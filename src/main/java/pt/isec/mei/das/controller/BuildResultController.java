package pt.isec.mei.das.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.dto.BuildResultDecoratedWithTimeInSeconds;
import pt.isec.mei.das.service.BuildService;

@RestController
@RequestMapping("/builds")
@AllArgsConstructor
public class BuildResultController {
  private final BuildService buildService;

  @GetMapping
  public ResponseEntity<List<BuildResultDTO>> getAllBuildResults() {
    return ResponseEntity.ok(buildService.findAllBuildResults());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BuildResultDTO> getBuildResultForProject(@PathVariable("id") Long id) {
    return ResponseEntity.ok(buildService.findBuildResultById(id));
  }

  @GetMapping("/{id}/time-converted")
  public ResponseEntity<BuildResultDecoratedWithTimeInSeconds> getDetailedBuildResultForProject(
      @PathVariable("id") Long id) {
    return ResponseEntity.ok(buildService.findDetailedBuildResultById(id));
  }

  @GetMapping("/project/{projectId}")
  public ResponseEntity<List<BuildResultDTO>> getBuildResult(
      @PathVariable("projectId") Long projectId) {
    return ResponseEntity.ok(buildService.findBuildResultsByProjectId(projectId));
  }
}
