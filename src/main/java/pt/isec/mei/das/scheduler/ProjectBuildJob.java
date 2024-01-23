package pt.isec.mei.das.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.isec.mei.das.service.BuildManager;
import pt.isec.mei.das.service.BuildService;

@Component
@RequiredArgsConstructor
public class ProjectBuildJob {
    private final BuildService buildService;

    @Scheduled(fixedRate = 60000)
    public void build() {
        BuildManager buildManager = BuildManager.getInstance();
        while (!buildManager.isEmpty()) {
            buildService.build(buildManager.dequeue());
        }
    }
}
