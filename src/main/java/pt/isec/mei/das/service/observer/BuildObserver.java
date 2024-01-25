package pt.isec.mei.das.service.observer;

import pt.isec.mei.das.entity.BuildResult;

public interface BuildObserver {

    void update(BuildResult buildResult);
}
