package pt.isec.mei.das.config;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class CompiledDirConfig {

  private final Path compiledFileStorageLocation;

  public CompiledDirConfig(FileStorageProperties fileStorageProperties) {
    this.compiledFileStorageLocation =
        Paths.get(fileStorageProperties.getCompiledDir()).toAbsolutePath().normalize();
  }

  @PostConstruct
  public void createDirectories() {
    try {
      Files.createDirectories(this.compiledFileStorageLocation);
    } catch (IOException e) {
      throw new RuntimeException("Could not create compiled directory!", e);
    }
  }
}
