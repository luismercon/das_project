package pt.isec.mei.das.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.isec.mei.das.config.FileStorageProperties;

@Service
@Slf4j
public class FileStorageService {

  private final Path fileStorageLocation;

  public FileStorageService(FileStorageProperties fileStorageProperties) {
    this.fileStorageLocation =
        Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
  }

  public String uploadFile(MultipartFile file) {

    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    log.info("Uploading through Service: " + fileName);

    try {
      Path targetLocation = fileStorageLocation.resolve(fileName);
      file.transferTo(targetLocation);

      String fileDownloadUri =
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/api/files/download/")
              .path(fileName)
              .toUriString();
      return "Upload completed! Download link: " + fileDownloadUri;

    } catch (IOException e) {
      return null;
    }
  }

  public Resource loadFileAsResource(String fileName) throws Exception {

    log.info("Downloading through Service: " + fileName);

    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        return resource;
      } else {
        throw new Exception("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new Exception("File not found " + fileName, ex);
    }
  }

  public Path uploadFileWithNewName(MultipartFile file, String newFileName) throws IOException {
    Path targetLocation = this.fileStorageLocation.resolve(newFileName);
    file.transferTo(targetLocation.toFile());
    return targetLocation;
  }
}
