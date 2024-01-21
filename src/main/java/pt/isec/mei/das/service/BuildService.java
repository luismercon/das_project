package pt.isec.mei.das.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.isec.mei.das.config.FileStorageProperties;
import pt.isec.mei.das.entity.Project;
import pt.isec.mei.das.exception.ProjectNotFoundException;
import pt.isec.mei.das.repository.ProjectRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class BuildService {

  private final ProjectRepository projectRepository;
  private final FileStorageProperties fileStorageProperties;

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

    // callCompiler(project.getFilePath());
  }

  private void callCompiler(String filePath) {

    ProcessBuilder builder = new ProcessBuilder("g++", filePath, "-o", "outputExecutable");
    builder.directory(new File(fileStorageProperties.getCompiledDir()));
    builder.redirectErrorStream(true);

    try {
      Process process = builder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      StringBuilder buildLogs = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        buildLogs.append(line).append("\n");
      }
      int exitCode = process.waitFor();

      System.out.println("======================");
      System.out.println("Chegou aqui com o exitCode: " + exitCode);
      System.out.println("======================");

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
