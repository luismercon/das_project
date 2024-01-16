package pt.isec.mei.das.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class BuildManager {

  private static BuildManager instance = new BuildManager();
  @Getter private Queue<String> filepathQueue;

  private BuildManager() {
    filepathQueue = new LinkedList<>();
  }

  public static BuildManager getInstance() {
    return instance;
  }

  public void enqueue(String filePath) {
    log.info("adding this filePath to queue: " + filePath);
    filepathQueue.add(filePath);
  }

  public String dequeue() {
    return filepathQueue.poll();
  }

  public boolean isEmpty() {
    return filepathQueue.isEmpty();
  }
}
