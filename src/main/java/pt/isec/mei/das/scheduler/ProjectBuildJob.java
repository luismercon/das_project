package pt.isec.mei.das.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.service.BuildManager;
import pt.isec.mei.das.service.BuildService;
import pt.isec.mei.das.service.observer.BuildObserver;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectBuildJob {
    private final BuildService buildService;
    private final List<BuildObserver> buildObservers;

    @Scheduled(fixedRate = 60000)
    public void build() {
        BuildManager buildManager = BuildManager.getInstance();
        while (!buildManager.isEmpty()) {
            BuildResult build = buildService.build(buildManager.dequeue());
            buildObservers.forEach(buildObserver -> buildObserver.update(build));
        }
    }
}
