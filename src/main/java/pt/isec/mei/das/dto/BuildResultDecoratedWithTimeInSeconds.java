package pt.isec.mei.das.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuildResultDecoratedWithTimeInSeconds implements BuildResultDecorator {

  private BuildResultDTO buildResultDTO;

  @Override
  public Long getId() {
    return buildResultDTO.getId();
  }

  @Override
  public Long getProjectId() {
    return buildResultDTO.getProjectId();
  }

  @Override
  public String getCompilationStatus() {
    return buildResultDTO.getCompilationStatus();
  }

  @Override
  public String getExecutableFilePath() {
    return buildResultDTO.getExecutableFilePath();
  }

  @Override
  public String getBuildLogs() {
    return buildResultDTO.getBuildLogs();
  }

  @Override
  public LocalDateTime getTimestamp() {
    return buildResultDTO.getTimestamp();
  }

  @Override
  public Long getCompilationTimeMs() {
    return buildResultDTO.getCompilationTimeMs();
  }

  public String getCompilationTimeSeconds() {

    BigDecimal timeInSeconds =
        BigDecimal.valueOf(buildResultDTO.getCompilationTimeMs())
            .divide(BigDecimal.valueOf(1000), 3, RoundingMode.CEILING);

    return "COMPILATION TIME IN SECONDS: " + timeInSeconds;
  }
}
