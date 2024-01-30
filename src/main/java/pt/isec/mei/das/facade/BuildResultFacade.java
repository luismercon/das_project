package pt.isec.mei.das.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.isec.mei.das.dto.BuildResultDTO;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.service.BuildService;
import pt.isec.mei.das.util.FieldFilterUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildResultFacade {

    private final BuildService buildService;

    public List<BuildResultDTO> findAllBuildResults() {
        List<BuildResult> buildResults = buildService.findAllBuildResults();
        return buildResults.stream()
                .map(b -> BuildResultDTO.builder()
                        .id(b.getId())
                        .projectId(b.getProject().getId())
                        .compilationTimeMs(b.getCompilationTimeInMs())
                        .compilationStatus(b.getCompilationStatus())
                        .executableFilePath(b.getExecutableFilePath())
                        .buildLogs(b.getBuildLogs())
                        .timestamp(b.getTimestamp())
                        .build())
                .toList();
    }

    public BuildResultDTO findBuildResultById(Long id) {
        BuildResult buildResult = buildService.findBuildResultById(id);
        return BuildResultDTO.builder()
                .id(buildResult.getId())
                .projectId(buildResult.getProject().getId())
                .compilationTimeMs(buildResult.getCompilationTimeInMs())
                .compilationStatus(buildResult.getCompilationStatus())
                .executableFilePath(buildResult.getExecutableFilePath())
                .buildLogs(buildResult.getBuildLogs())
                .timestamp(buildResult.getTimestamp())
                .build();
    }

    public Object findBuildResultById(Long id, List<String> fields) {
        BuildResult buildResult = buildService.findBuildResultById(id);
        if (fields == null || fields.isEmpty()) {
            return BuildResultDTO.builder()
                    .id(buildResult.getId())
                    .projectId(buildResult.getProject().getId())
                    .compilationTimeMs(buildResult.getCompilationTimeInMs())
                    .compilationStatus(buildResult.getCompilationStatus())
                    .executableFilePath(buildResult.getExecutableFilePath())
                    .buildLogs(buildResult.getBuildLogs())
                    .timestamp(buildResult.getTimestamp())
                    .build();
        } else {
            return FieldFilterUtil.filterFields(buildResult, fields);
        }
    }

    public String findStatusBuildResultById(Long id) {
        return buildService.findStatusBuildResultById(id);
    }

    public List<BuildResultDTO> findBuildResultsByProjectId(Long projectId) {
        List<BuildResult> buildResults = buildService.findBuildResultsByProjectId(projectId);
        return buildResults.stream()
                .map(
                        b ->
                                BuildResultDTO.builder()
                                        .id(b.getId())
                                        .projectId(b.getProject().getId())
                                        .compilationTimeMs(b.getCompilationTimeInMs())
                                        .compilationStatus(b.getCompilationStatus())
                                        .executableFilePath(b.getExecutableFilePath())
                                        .buildLogs(b.getBuildLogs())
                                        .timestamp(b.getTimestamp())
                                        .build())
                .toList();
    }

    public BuildResultDTO cancelBuild(long id) {
        BuildResult cancelledBuild = buildService.cancelBuild(id);
        return BuildResultDTO.builder()
                .id(cancelledBuild.getId())
                .projectId(cancelledBuild.getProject().getId())
                .compilationTimeMs(cancelledBuild.getCompilationTimeInMs())
                .compilationStatus(cancelledBuild.getCompilationStatus())
                .executableFilePath(cancelledBuild.getExecutableFilePath())
                .buildLogs(cancelledBuild.getBuildLogs())
                .timestamp(cancelledBuild.getTimestamp())
                .build();
    }

    public BuildResultDTO submitBuild(long projectId, boolean isNotificationNeeded) {
        BuildResult buildResult = buildService.submitBuild(projectId, isNotificationNeeded);
        return BuildResultDTO.builder()
                .id(buildResult.getId())
                .projectId(buildResult.getProject().getId())
                .compilationStatus(buildResult.getCompilationStatus())
                .timestamp(buildResult.getTimestamp())
                .build();
    }

    public BuildResultDTO retryBuild(long buildId) {
        BuildResult buildResult = buildService.retryBuild(buildId);
        return BuildResultDTO.builder()
                .id(buildResult.getId())
                .projectId(buildResult.getProject().getId())
                .compilationStatus(buildResult.getCompilationStatus())
                .timestamp(buildResult.getTimestamp())
                .build();
    }

    public void delete(long id) {
        buildService.delete(id);
    }
}
