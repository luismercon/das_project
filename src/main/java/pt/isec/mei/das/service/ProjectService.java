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
import pt.isec.mei.das.exception.EntityNotFoundException;
import pt.isec.mei.das.repository.BuildResultRepository;
import pt.isec.mei.das.repository.ProjectRepository;

@Service
@AllArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final FileStorageService fileStorageService;
  private final BuildResultRepository buildResultRepository;
  private final FileStorageProperties fileStorageProperties;

  public List<ProjectDTO> getlAllProjects() {
    List<Project> projects = projectRepository.findAll();

    return projects.stream()
        .map(
            p ->
                ProjectDTO.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .createdAt(p.getCreatedAt())
                    .file(null)
                    .build())
        .toList();
  }

  @Transactional
  public ProjectDTO createProject(ProjectDTO request) {
    Project project = new Project();
    project.setName(request.getName());
    project.setCreatedAt(LocalDateTime.now());

    // Save the project first to get the generated ID
    Project projectSaved = projectRepository.save(project);

    try {
      String originalFileName = StringUtils.cleanPath(request.getFile().getOriginalFilename());
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
        .name(projectSaved.getName())
        .createdAt(projectSaved.getCreatedAt())
        .build();
  }

  public ProjectDTO getProjectById(long id) {
    Project project =
        projectRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

    return ProjectDTO.builder()
        .id(project.getId())
        .name(project.getName())
        .createdAt(project.getCreatedAt())
        .build();
  }
}
