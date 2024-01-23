package pt.isec.mei.das.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.isec.mei.das.config.FileStorageProperties;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.entity.Project;
import pt.isec.mei.das.enums.CompilationStatus;
import pt.isec.mei.das.exception.EntityNotFoundException;
import pt.isec.mei.das.repository.BuildResultRepository;
import pt.isec.mei.das.repository.ProjectRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BuildService {

    private final ProjectRepository projectRepository;
    private final FileStorageProperties fileStorageProperties;
    private final BuildResultRepository buildResultRepository;

    @Transactional
    public BuildResultDTO submitBuild(long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        BuildResult buildResult = new BuildResult();
        buildResult.setProject(project);
        buildResult.setCompilationStatus(CompilationStatus.IN_PROGRESS.name());
        buildResult.setTimestamp(LocalDateTime.now());
        buildResultRepository.save(buildResult);

        BuildManager.getInstance().enqueue(buildResult);

        log.info("Project {} is added to the build queue", project.getName());

        return BuildResultDTO.builder()
                .id(buildResult.getId())
                .projectId(buildResult.getProject().getId())
                .compilationStatus(buildResult.getCompilationStatus())
                .timestamp(buildResult.getTimestamp())
                .build();
    }

    @Transactional
    public void build(BuildResult build) {
        String outputFileName = build.getId() + "_" + build.getProject().getName() + "_compiled";
        File outputDir = new File(fileStorageProperties.getCompiledDir());

        ProcessBuilder processBuilder = new ProcessBuilder("g++", build.getProject().getFilePath(), "-o", outputFileName);
        processBuilder.directory(outputDir);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            String buildLogs = getBuildLogs(process);

            BuildResult buildResult = buildResultRepository.findById(build.getId())
                    .orElseThrow(() -> new EntityNotFoundException("BuildResults not found for project with id: " + build.getProject().getId()));
            buildResult.setBuildLogs(buildLogs);
            buildResult.setTimestamp(LocalDateTime.now());

            if (exitCode == 0) {
                buildResult.setExecutableFilePath(new File(outputDir, outputFileName).getAbsolutePath());
                buildResult.setCompilationStatus(CompilationStatus.SUCCESS.name());
            } else {
                buildResult.setCompilationStatus(CompilationStatus.FAILURE.name());
            }
            buildResultRepository.save(buildResult);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<BuildResultDTO> findBuildResultsByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        List<BuildResult> buildResults = buildResultRepository.findBuildResultByProject(project);
        if (buildResults.isEmpty()) {
            throw new EntityNotFoundException("BuildResult not found for project with id: " + projectId);
        }
        return buildResults.stream()
                .map(b -> BuildResultDTO.builder()
                                .id(b.getId())
                                .projectId(b.getProject().getId())
                                .sourceCodeHash(b.getSourceCodeHash())
                                .compilationStatus(b.getCompilationStatus())
                                .executableFilePath(b.getExecutableFilePath())
                                .buildLogs(b.getBuildLogs())
                                .timestamp(b.getTimestamp())
                                .build())
                .toList();
    }

    public List<BuildResultDTO> findAllBuildResults() {
        List<BuildResult> buildResults = buildResultRepository.findAll();

        return buildResults.stream()
                .map(
                        b ->
                                BuildResultDTO.builder()
                                        .id(b.getId())
                                        .projectId(b.getProject().getId())
                                        .sourceCodeHash(b.getSourceCodeHash())
                                        .compilationStatus(b.getCompilationStatus())
                                        .executableFilePath(b.getExecutableFilePath())
                                        .buildLogs(b.getBuildLogs())
                                        .timestamp(b.getTimestamp())
                                        .build())
                .toList();
    }

    public BuildResultDTO findBuildResultById(Long id) {
        BuildResult buildResult = buildResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BuildResult with id: " + id + " not found"));
        return BuildResultDTO.builder()
                .id(buildResult.getId())
                .projectId(buildResult.getProject().getId())
                .sourceCodeHash(buildResult.getSourceCodeHash())
                .compilationStatus(buildResult.getCompilationStatus())
                .executableFilePath(buildResult.getExecutableFilePath())
                .buildLogs(buildResult.getBuildLogs())
                .timestamp(buildResult.getTimestamp())
                .build();
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
}
