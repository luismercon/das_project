package pt.isec.mei.das.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("file")
@Getter
@Setter
public class FileStorageProperties {
  private String uploadDir;
}
