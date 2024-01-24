package pt.isec.mei.das.service;

import jakarta.transaction.Transactional;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pt.isec.mei.das.config.FileStorageProperties;
import pt.isec.mei.das.dto.ProjectDTO;
import pt.isec.mei.das.entity.Project;
import pt.isec.mei.das.entity.ProjectLanguage;
import pt.isec.mei.das.exception.EntityNotFoundException;
import pt.isec.mei.das.repository.BuildResultRepository;
import pt.isec.mei.das.repository.ProjectLanguageRepository;
import pt.isec.mei.das.repository.ProjectRepository;

@Service
@AllArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final FileStorageService fileStorageService;
  private final BuildResultRepository buildResultRepository;
  private final FileStorageProperties fileStorageProperties;
  private final ProjectLanguageRepository projectLanguageRepository;

  public List<ProjectDTO> getlAllProjects() {
    List<Project> projects = projectRepository.findAll();

    return projects.stream()
        .map(
            p ->
                ProjectDTO.builder()
                    .id(p.getId())
                    .programmingLanguage(p.getProjectLanguage().getProgrammingLanguage())
                    .name(p.getName())
                    .createdAt(p.getCreatedAt())
                    .file(null)
                    .build())
        .toList();
  }

  @Transactional
  public ProjectDTO createProject(ProjectDTO request) {

    String originalFileName = StringUtils.cleanPath(request.getFile().getOriginalFilename());

    ProjectLanguage projectLanguage = verifyProgrammingLanguage(originalFileName);

    Project project = new Project();
    project.setName(request.getName());
    project.setCreatedAt(LocalDateTime.now());
    project.setProjectLanguage(projectLanguage);

    // Save the project first to get the generated ID
    Project projectSaved = projectRepository.save(project);

    try {
      String newFileName = projectSaved.getId() + "_" + originalFileName;
      Path newFilePath = fileStorageService.uploadFileWithNewName(request.getFile(), newFileName);
      projectSaved.setFilePath(newFilePath.toString()); // Update the project with the new file path
      projectRepository.save(projectSaved); // Save the project again with the updated file path
    } catch (Exception e) {
      // Handle file upload error
      throw new RuntimeException("Failed to upload file", e);
    }

    return ProjectDTO.builder()
        .id(project.getId())
        .programmingLanguage(project.getProjectLanguage().getProgrammingLanguage())
        .name(projectSaved.getName())
        .createdAt(projectSaved.getCreatedAt())
        .build();
  }

  private ProjectLanguage verifyProgrammingLanguage(String fileName) {

    // Ensure the fileName is not empty
    assert !fileName.isEmpty();

    // Find the last index of the dot character
    int dotIndex = fileName.lastIndexOf('.');

    // Check if a valid file extension is present
    if (dotIndex <= 0 || dotIndex >= fileName.length() - 1) {
      throw new IllegalArgumentException("Invalid file name: " + fileName);
    }

    // Extract the file extension
    String fileExtension = fileName.substring(dotIndex + 1);

    return projectLanguageRepository
        .findByFileExtension(fileExtension)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    String.format(
                        "Programming Language for [ %s ] file extension not found",
                        fileExtension)));
  }

  public ProjectDTO getProjectById(long id) {
    Project project =
        projectRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

    return ProjectDTO.builder()
        .id(project.getId())
        .programmingLanguage(project.getProjectLanguage().getProgrammingLanguage())
        .name(project.getName())
        .createdAt(project.getCreatedAt())
        .build();
  }
}
