package pt.isec.mei.das.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pt.isec.mei.das.enums.CompilationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class BuildResultDTO {
    private Long id;
    private Long projectId;
    private String sourceCodeHash;
    private String compilationStatus;
    private String executableFilePath;
    private String buildLogs;
    private LocalDateTime timestamp;
}
