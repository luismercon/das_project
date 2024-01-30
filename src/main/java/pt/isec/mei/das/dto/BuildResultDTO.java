package pt.isec.mei.das.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class BuildResultDTO implements BuildResultDecorator {
  private Long id;
  private Long projectId;
  private Long compilationTimeMs;
  private String compilationStatus;
  private String executableFilePath;
  private String buildLogs;
  private LocalDateTime timestamp;
}
