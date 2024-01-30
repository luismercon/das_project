package pt.isec.mei.das.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pt.isec.mei.das.config.FileStorageProperties;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.entity.Project;
import pt.isec.mei.das.enums.CompilationStatus;
import pt.isec.mei.das.exception.BuildCancelledException;
import pt.isec.mei.das.exception.EntityNotFoundException;
import pt.isec.mei.das.exception.StateChangeNotAllowedException;
import pt.isec.mei.das.repository.BuildResultRepository;
import pt.isec.mei.das.repository.ProjectRepository;
import pt.isec.mei.das.service.compiler.Compiler;
import pt.isec.mei.das.service.compiler.CompilerFactory;

import static pt.isec.mei.das.enums.CompilationStatus.CANCELLED;
import static pt.isec.mei.das.enums.CompilationStatus.FAILURE;
import static pt.isec.mei.das.enums.CompilationStatus.IN_PROGRESS;
import static pt.isec.mei.das.enums.CompilationStatus.IN_QUEUE;
import static pt.isec.mei.das.enums.CompilationStatus.SUCCESS;

@Slf4j
@Service
@AllArgsConstructor
public class BuildService {

    private final ProjectRepository projectRepository;
    private final FileStorageProperties fileStorageProperties;
    private final BuildResultRepository buildResultRepository;

    @Transactional
    public BuildResult submitBuild(long projectId, boolean isNotificationNeeded) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Project not found with id: " + projectId));

        BuildResult buildResult = new BuildResult();
        buildResult.setProject(project);
        buildResult.setCompilationStatus(IN_QUEUE.name());
        buildResult.setTimestamp(LocalDateTime.now());
        buildResult.setNotificationNeeded(isNotificationNeeded);

        BuildResult build = buildResultRepository.save(buildResult);
        BuildManager.getInstance().enqueue(build);

        log.info("Project {} is added to the build queue", project.getName());

        return build;
    }

    @Transactional
    public BuildResult build(BuildResult build) {

        long start = System.currentTimeMillis();
        System.out.println("Start: " + start);

        String outputFileName = build.getId() + "_" + build.getProject().getName() + "_compiled";
        File outputDir = new File(fileStorageProperties.getCompiledDir());

        Compiler compiler =
                CompilerFactory.getCompiler(
                        build.getProject().getProjectLanguage().getProgrammingLanguage());

        try {
            Process startedPocess =
                    compiler.compile(build.getProject().getFilePath(), outputFileName, outputDir);

            int exitCode = checkForCancellation(build, startedPocess);

            String currentStatus = findStatusBuildResultById(build.getId());
            if (currentStatus.equals(CANCELLED.name())) {
                throw new BuildCancelledException("build " + build.getId()+ " cancelled");
            }

            String buildLogs = getBuildLogs(startedPocess);

            BuildResult buildResult = findBuildResultById(build.getId());
            buildResult.setBuildLogs(buildLogs);
            buildResult.setTimestamp(LocalDateTime.now());

            if (exitCode == 0) {
                buildResult.setExecutableFilePath(new File(outputDir, outputFileName).getAbsolutePath());
                buildResult.setCompilationStatus(SUCCESS.name());
            } else {
                buildResult.setCompilationStatus(CompilationStatus.FAILURE.name());
            }

            long end = System.currentTimeMillis();
            System.out.println("End: " + end);
            long time = (end - start);
            System.out.println("Time in millis: " + time);

            buildResult.setCompilationTimeInMs(time);

            return buildResultRepository.save(buildResult);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<BuildResult> findBuildResultsByProjectId(Long projectId) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Project not found with id: " + projectId));
        List<BuildResult> buildResults = buildResultRepository.findBuildResultByProject(project);
        if (buildResults.isEmpty()) {
            throw new EntityNotFoundException("BuildResult not found for project with id: " + projectId);
        }
        return buildResults;
    }

    public List<BuildResult> findAllBuildResults() {
        return buildResultRepository.findAll();
    }

    public BuildResult findBuildResultById(Long id) {
        return buildResultRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("BuildResult with id: " + id + " not found"));
    }

    public String findStatusBuildResultById(Long id) {

        String status = buildResultRepository.findStatusById(id);

        if (status == null) {
            return String.format("Build id: %d not found", id);
        }

        return status;
    }

    @Transactional
    public BuildResult cancelBuild(long buildId) {
        BuildResult buildResult = findBuildResultById(buildId);
        List<String> noCancelStatuses = Arrays.asList(CANCELLED.name(), FAILURE.name(), SUCCESS.name());

        if (noCancelStatuses.contains(buildResult.getCompilationStatus())) {
            throw new StateChangeNotAllowedException("BuildResult with id " + buildId + " is already processed. " +
                    "The current status is " + buildResult.getCompilationStatus());
        }

        buildResult.setCompilationStatus(CompilationStatus.CANCELLED.name());
        return buildResultRepository.save(buildResult);
    }

    @Transactional
    public BuildResult retryBuild(long buildId) {
        BuildResult buildResult = findBuildResultById(buildId);
        List<String> noRetryStatuses = Arrays.asList(IN_QUEUE.name(), IN_PROGRESS.name(), SUCCESS.name());

        if (noRetryStatuses.contains(buildResult.getCompilationStatus())) {
            throw new StateChangeNotAllowedException("Build with id " + buildId + " is in the queue or finished successfully");
        }

        buildResult.setCompilationStatus(IN_QUEUE.name());
        buildResultRepository.save(buildResult);
        BuildManager.getInstance().enqueue(buildResult);

        return buildResult;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BuildResult updateStatus(BuildResult buildResult, CompilationStatus compilationStatus) {
        buildResult.setCompilationStatus(compilationStatus.name());
        return buildResultRepository.save(buildResult);
    }

    @Transactional
    public void delete(long id) {
        BuildResult buildResult = findBuildResultById(id);
        buildResultRepository.delete(buildResult);
    }

    private String getBuildLogs(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder buildLogs = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            buildLogs.append(line).append("\n");
        }
        return buildLogs.toString();
    }

    private int checkForCancellation(BuildResult build, Process startedPocess) {
        ExecutorService statusChecker = Executors.newSingleThreadExecutor();
        Future<?> statusCheckFuture = statusChecker.submit(() -> {
            while (startedPocess.isAlive()) {
                String currentStatus = findStatusBuildResultById(build.getId());
                if (CompilationStatus.CANCELLED.name().equals(currentStatus)) {
                    startedPocess.destroy();
                    throw new BuildCancelledException("build " + build.getId()+ " cancelled");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new BuildCancelledException("");
                }
            }
        });

        int exitCode;
        try {
            exitCode = startedPocess.waitFor();
            statusCheckFuture.get(1, TimeUnit.SECONDS); // Wait for status checking thread to complete
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new BuildCancelledException("build " + build.getId()+ " cancelled");
        }

        statusCheckFuture.cancel(true); // Cancel the status checking thread
        statusChecker.shutdown();

        return exitCode;
    }
}
