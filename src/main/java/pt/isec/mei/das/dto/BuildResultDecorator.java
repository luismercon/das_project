package pt.isec.mei.das.dto;

import java.time.LocalDateTime;

public interface BuildResultDecorator {
  Long getId();

  Long getProjectId();

  String getCompilationStatus();

  String getExecutableFilePath();

  String getBuildLogs();

  LocalDateTime getTimestamp();

  Long getCompilationTimeMs();
}
