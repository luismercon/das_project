package pt.isec.mei.das.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.enums.CompilationStatus;
import pt.isec.mei.das.service.BuildManager;
import pt.isec.mei.das.service.BuildService;
import pt.isec.mei.das.service.observer.BuildObserver;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectBuildJob {
    private final BuildService buildService;
    private final List<BuildObserver> buildObservers;

    @Scheduled(fixedRate = 30000)
    public void build() {
        BuildManager buildManager = BuildManager.getInstance();
        while (!buildManager.isEmpty()) {
            BuildResult queuingBuild = buildManager.dequeue();
            String compilationStatus = buildService.findStatusBuildResultById(queuingBuild.getId());

            if (compilationStatus.equals(CompilationStatus.IN_QUEUE.name())) {
                BuildResult build = buildService.build(
                        buildService.updateStatus(queuingBuild, CompilationStatus.IN_PROGRESS)
                );
                buildObservers.forEach(buildObserver -> buildObserver.update(build));
            }
        }
    }
}
