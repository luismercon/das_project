package pt.isec.mei.das.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public abstract class AbstractBuildResultDecorator implements BuildResultDecorator {

  protected BuildResultDecorator decoratedBuildResult;

  @Override
  public Long getId() {
    return decoratedBuildResult.getId();
  }

  @Override
  public Long getProjectId() {
    return decoratedBuildResult.getProjectId();
  }

  @Override
  public String getCompilationStatus() {
    return decoratedBuildResult.getCompilationStatus();
  }

  @Override
  public String getExecutableFilePath() {
    return decoratedBuildResult.getExecutableFilePath();
  }

  @Override
  public String getBuildLogs() {
    return decoratedBuildResult.getBuildLogs();
  }

  @Override
  public LocalDateTime getTimestamp() {
    return decoratedBuildResult.getTimestamp();
  }

  @Override
  public Long getCompilationTimeMs() {
    return decoratedBuildResult.getCompilationTimeMs();
  }
}
