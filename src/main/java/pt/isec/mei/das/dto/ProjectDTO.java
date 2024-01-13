package pt.isec.mei.das.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ProjectDTO {
  private Long id;
  private String name;
  private LocalDateTime createdAt;
  private MultipartFile file;
}
