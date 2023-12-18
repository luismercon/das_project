package pt.isec.mei.das.config;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class UploadDirConfig {

  private final Path fileStorageLocation;

  public UploadDirConfig(FileStorageProperties fileStorageProperties) {
    this.fileStorageLocation =
        Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
  }

  @PostConstruct
  public void createDirectories() {
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (IOException e) {
      throw new RuntimeException("Could not create upload directory!", e);
    }
  }
}
