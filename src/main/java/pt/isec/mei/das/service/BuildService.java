package pt.isec.mei.das.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.isec.mei.das.config.FileStorageProperties;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.entity.Project;
import pt.isec.mei.das.exception.ProjectNotFoundException;
import pt.isec.mei.das.repository.BuildResultRepository;
import pt.isec.mei.das.repository.ProjectRepository;

@Service
@AllArgsConstructor
public class BuildService {

    private final ProjectRepository projectRepository;
    private final FileStorageProperties fileStorageProperties;
    private final BuildResultRepository buildResultRepository;

    public void triggerBuild(long projectId) {

        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(
                                () -> new ProjectNotFoundException("Project not found with id: " + projectId));

        BuildManager.getInstance().enqueue(project.getFilePath());

        System.out.println("====================");
        System.out.println("UNDER CONSTRUCTION: TRIGGER BUILD -- PROJECT ID: " + projectId);
        System.out.println("FILEPATH: " + project.getFilePath());
        System.out.println("====================");

        for (String q : BuildManager.getInstance().getFilepathQueue()) {
            System.out.println("File in queue: " + q);
        }

        System.out.println(BuildManager.getInstance().getFilepathQueue().toString());

        build(project);
    }

    private void build(Project project) {
        String outputFileName = "outputExecutable";
        File outputDir = new File(fileStorageProperties.getCompiledDir());

        ProcessBuilder processBuilder = new ProcessBuilder("g++", project.getFilePath(), "-o", outputFileName);
        processBuilder.directory(outputDir);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            String buildLogs = getBuildLogs(process);

            BuildResult buildResult = new BuildResult();
            buildResult.setProject(project);
            buildResult.setBuildLogs(buildLogs);
            buildResult.setTimestamp(LocalDateTime.now());

            if (exitCode == 0) {
                buildResult.setExecutableFilePath(new File(outputDir, outputFileName).getAbsolutePath());
                buildResult.setCompilationStatus(true);
            } else {
                // todo do we really need these columns? can we just always use 'buildLogs' instead?
                buildResult.setErrorMessages(buildLogs);
                buildResult.setWarningMessages(buildLogs);
            }
            buildResultRepository.save(buildResult);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
                                        .errorMessages(b.getErrorMessages())
                                        .warningMessages(b.getWarningMessages())
                                        .executableFilePath(b.getExecutableFilePath())
                                        .buildLogs(b.getBuildLogs())
                                        .timestamp(b.getTimestamp())
                                        .build())
                .toList();
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
