package pt.isec.mei.das.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
