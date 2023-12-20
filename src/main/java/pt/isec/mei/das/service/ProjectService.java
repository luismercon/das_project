package pt.isec.mei.das.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.isec.mei.das.dto.ProjectDTO;
import pt.isec.mei.das.entity.Project;
import pt.isec.mei.das.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;

  public List<ProjectDTO> getlAllProjects() {
    List<Project> projects = projectRepository.findAll();

    return projects.stream()
        .map(p -> new ProjectDTO(p.getId(), p.getName(), p.getCreatedAt()))
        .toList();
  }

  @Transactional
  public ProjectDTO createProject(ProjectDTO request) {

    Project project = new Project();
    project.setName(request.name());
    project.setCreatedAt(LocalDateTime.now());

    Project projectSaved = projectRepository.save(project);

    return new ProjectDTO(projectSaved.getId(), project.getName(), project.getCreatedAt());
  }
}
