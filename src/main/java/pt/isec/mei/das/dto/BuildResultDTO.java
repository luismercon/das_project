package pt.isec.mei.das.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pt.isec.mei.das.enums.CompilationStatus;

import java.time.LocalDateTime;

@Data
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

    // No-argument constructor
    public BuildResultDTO() {
    }

    // Existing constructor
    public BuildResultDTO(Long id, Long projectId, String sourceCodeHash, String compilationStatus, String executableFilePath, String buildLogs, LocalDateTime timestamp) {
        this.id = id;
        this.projectId = projectId;
        this.sourceCodeHash = sourceCodeHash;
        this.compilationStatus = compilationStatus;
        this.executableFilePath = executableFilePath;
        this.buildLogs = buildLogs;
        this.timestamp = timestamp;
    }

}

