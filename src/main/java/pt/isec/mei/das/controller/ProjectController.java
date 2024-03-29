package pt.isec.mei.das.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.dto.ProjectDTO;
import pt.isec.mei.das.facade.BuildResultFacade;
import pt.isec.mei.das.service.ProjectService;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final BuildResultFacade buildResultFacade;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getlAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping("/{id}/build")
    public BuildResultDTO submitBuild(@PathVariable long id,
                                      @RequestParam(required = false) boolean isNotificationNeeded) {
        return buildResultFacade.submitBuild(id, isNotificationNeeded);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectDTO> createProject(@ModelAttribute ProjectDTO request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }
}
