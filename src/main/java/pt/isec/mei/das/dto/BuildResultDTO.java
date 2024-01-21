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
public class BuildResultDTO {
    private Long id;
    private Long projectId;
    private String sourceCodeHash;
    private Boolean compilationStatus;
    private String errorMessages;
    private String warningMessages;
    private String executableFilePath;
    private String buildLogs;
    private LocalDateTime timestamp;
}
