package pt.isec.mei.das.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pt.isec.mei.das.entity.BuildResult;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class BuildManager {

  private static BuildManager instance = new BuildManager();
  @Getter
  private final AbstractQueue<BuildResult> buildQueue;

  private BuildManager() {
    buildQueue = new ConcurrentLinkedQueue<>();
  }

  public static BuildManager getInstance() {
    return instance;
  }

  public void enqueue(BuildResult buildResult) {
    log.info("adding this filePath to queue: " + buildResult);
    buildQueue.add(buildResult);
  }

  public BuildResult dequeue() {
    return buildQueue.poll();
  }

  public boolean isEmpty() {
    return buildQueue.isEmpty();
  }
}
