package pt.isec.mei.das.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.dto.BuildResultDecoratedWithTimeInSeconds;
import pt.isec.mei.das.dto.BuildResultDecorator;
import pt.isec.mei.das.enums.CompilationStatus;
import pt.isec.mei.das.facade.BuildResultFacade;

@RestController
@RequestMapping("/builds")
@AllArgsConstructor
public class BuildResultController {
    private final BuildResultFacade buildResultFacade;

    @GetMapping
    public ResponseEntity<List<BuildResultDTO>> getAllBuildResults() {
        return ResponseEntity.ok(buildResultFacade.findAllBuildResults());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildResultDTO> getBuildResult(@PathVariable("id") Long id) {
        return ResponseEntity.ok(buildResultFacade.findBuildResultById(id));
    }

    @GetMapping("/field-mask/{id}")
    public ResponseEntity<Object> getBuildResulFieldMask(@PathVariable("id") Long id,
                                                         @RequestParam(required = false) List<String> fields
    ) {
        return ResponseEntity.ok(buildResultFacade.findBuildResultById(id, fields));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getStatusBuildResultForProject(@PathVariable("id") Long id) {
        return ResponseEntity.ok(buildResultFacade.findStatusBuildResultById(id));

    }

    @GetMapping("/{id}/time-converted")
    public ResponseEntity<?> getDetailedBuildResultForProject(@PathVariable("id") Long id) {
        BuildResultDTO buildResultById = buildResultFacade.findBuildResultById(id);

        if (buildResultById.getCompilationStatus().equals(CompilationStatus.IN_QUEUE.name())
                || buildResultById.getCompilationStatus().equals(CompilationStatus.IN_PROGRESS.name())) {
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
        return ResponseEntity.ok(buildResultFacade.findBuildResultsByProjectId(projectId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BuildResultDTO> cancelBuild(@PathVariable long id) {
        BuildResultDTO cancelledBuild = buildResultFacade.cancelBuild(id);
        return ResponseEntity.ok(cancelledBuild);
    }

    @PutMapping("/{id}/retry")
    public ResponseEntity<BuildResultDTO> retryBuild(@PathVariable long id) {
        BuildResultDTO retriedBuild = buildResultFacade.retryBuild(id);
        return ResponseEntity.ok(retriedBuild);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        buildResultFacade.delete(id);
        return ResponseEntity.ok().build();
    }
}
