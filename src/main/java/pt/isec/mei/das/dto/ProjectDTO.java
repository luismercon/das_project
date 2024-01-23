package pt.isec.mei.das.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ProjectDTO {
  private Long id;
  private String programmingLanguage;
  private String name;
  private LocalDateTime createdAt;
  private MultipartFile file;
}
