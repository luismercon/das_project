package pt.isec.mei.das.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.service.BuildService;

import java.util.List;


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
    public ResponseEntity<BuildResultDTO> getBuildResultForProject(@PathVariable("id") Long id, @RequestParam(required = false) List<String> fields) {
        BuildResultDTO buildResult = buildService.findBuildResultById(id);
        if (fields != null && !fields.isEmpty()) {
            buildResult = filterFields(buildResult, fields);
        }
        return ResponseEntity.ok(buildResult);
    }

    private BuildResultDTO filterFields(BuildResultDTO buildResult, List<String> fields) {
        // Create a new BuildResultDTO object
        BuildResultDTO filteredBuildResult = new BuildResultDTO();

        // Check each field in the fields list and copy the corresponding property from buildResult to filteredBuildResult
        for (String field : fields) {
            switch (field) {
                case "id":
                    filteredBuildResult.setId(buildResult.getId());
                    break;
                case "projectId":
                    filteredBuildResult.setProjectId(buildResult.getProjectId());
                    break;
                case "sourceCodeHash":
                    filteredBuildResult.setSourceCodeHash(buildResult.getSourceCodeHash());
                    break;
                case "compilationStatus":
                    filteredBuildResult.setCompilationStatus(buildResult.getCompilationStatus());
                    break;
                case "executableFilePath":
                    filteredBuildResult.setExecutableFilePath(buildResult.getExecutableFilePath());
                    break;
                case "buildLogs":
                    filteredBuildResult.setBuildLogs(buildResult.getBuildLogs());
                    break;
                case "timestamp":
                    filteredBuildResult.setTimestamp(buildResult.getTimestamp());
                    break;
                default:
                    // Handle the case where a client might ask for a field that does not exist
                    break;
            }
        }

        return filteredBuildResult;
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<BuildResultDTO>> getBuildResult(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(buildService.findBuildResultsByProjectId(projectId));
    }
}
