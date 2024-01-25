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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public ResponseEntity<Map<String, Object>> getBuildResultForProject(@PathVariable("id") Long id, @RequestParam(required = false) List<String> fields) {
        BuildResultDTO buildResult = buildService.findBuildResultById(id);
        Map<String, Object> filteredResult = new HashMap<>();
        if (fields != null && !fields.isEmpty()) {
            filteredResult = filterFields(buildResult, fields);
        }
        return ResponseEntity.ok(filteredResult);
    }

    private Map<String, Object> filterFields(BuildResultDTO buildResult, List<String> fields) {
        Map<String, Object> filteredResult = new HashMap<>();
        for (String field : fields) {
            switch (field) {
                case "id":
                    filteredResult.put("id", buildResult.getId());
                    break;
                case "projectId":
                    filteredResult.put("projectId", buildResult.getProjectId());
                    break;
                case "sourceCodeHash":
                    filteredResult.put("sourceCodeHash", buildResult.getSourceCodeHash());
                    break;
                case "compilationStatus":
                    filteredResult.put("compilationStatus", buildResult.getCompilationStatus());
                    break;
                case "executableFilePath":
                    filteredResult.put("executableFilePath", buildResult.getExecutableFilePath());
                    break;
                case "buildLogs":
                    filteredResult.put("buildLogs", buildResult.getBuildLogs());
                    break;
                case "timestamp":
                    filteredResult.put("timestamp", buildResult.getTimestamp());
                    break;
                default:

                    break;
            }
        }
        return filteredResult;
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<BuildResultDTO>> getBuildResult(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(buildService.findBuildResultsByProjectId(projectId));
    }
}
