package pt.isec.mei.das.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.dto.BuildResultDecoratedWithTimeInSeconds;
import pt.isec.mei.das.dto.BuildResultDecorator;
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
    public ResponseEntity<BuildResultDTO> getBuildResult(@PathVariable("id") Long id) {
        return ResponseEntity.ok(buildService.findBuildResultById(id));
    }

    @GetMapping("/field-mask/{id}")
    public ResponseEntity<Object> getBuildResulFieldMask(@PathVariable("id") Long id,
                                                         @RequestParam(required = false) List<String> fields
    ) {
        return ResponseEntity.ok(buildService.findBuildResultById(id, fields));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getStatusBuildResultForProject(@PathVariable("id") Long id) {
        return ResponseEntity.ok(buildService.findStatusBuildResultById(id));
    }

    @GetMapping("/{id}/time-converted")
    public ResponseEntity<?> getDetailedBuildResultForProject(@PathVariable("id") Long id) {
        BuildResultDTO buildResultById = buildService.findBuildResultById(id);

        if (buildResultById.getCompilationStatus().equals("NOT_STARTED")
                || buildResultById.getCompilationStatus().equals("IN_PROGRESS")) {
            String response =
                    "Your process didn't finish. You can check the status at: http://localhost:8080/builds/"
                            + id
                            + "/status";

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        BuildResultDecorator decoratedResult = new BuildResultDecoratedWithTimeInSeconds(buildResultById);
        return ResponseEntity.ok(decoratedResult);
    }


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<BuildResultDTO>> getBuildResultForProject(
            @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(buildService.findBuildResultsByProjectId(projectId));
    }
}
