package pt.isec.mei.das.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.isec.mei.das.config.FileStorageProperties;

@Service
public class FileStorageService {

  private final Path fileStorageLocation;

  public FileStorageService(FileStorageProperties fileStorageProperties) {
    this.fileStorageLocation =
        Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
  }

  public String uploadFile(MultipartFile file) {

    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    System.out.println("==========================================");
    System.out.println("Uploading through Service: " + fileName);
    System.out.println("==========================================");

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

    System.out.println("==========================================");
    System.out.println("Downloading through Service: " + fileName);
    System.out.println("==========================================");

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
}
