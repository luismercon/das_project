package pt.isec.mei.das.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.mei.das.dto.ProjectDTO;
import pt.isec.mei.das.entity.Project;
import pt.isec.mei.das.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping
  public ResponseEntity<List<ProjectDTO>> getAllProjects() {
    return ResponseEntity.ok(projectService.getlAllProjects());
  }

  @PostMapping
  private ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO request) {
    return ResponseEntity.ok(projectService.createProject(request));
  }
}
