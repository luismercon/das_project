package pt.isec.mei.das.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pt.isec.mei.das.config.FileStorageProperties;
import pt.isec.mei.das.service.FileStorageService;

@RestController
@RequestMapping("/api/files")
public class FileStorageController {

  private final Path fileStorageLocation;
  private final FileStorageService fileStorageService;

  public FileStorageController(
      FileStorageProperties fileStorageProperties, FileStorageService fileStorageService) {
    this.fileStorageLocation =
        Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    this.fileStorageService = fileStorageService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

    String serviceResponse = fileStorageService.uploadFile(file);

    if (serviceResponse != null) {
      return ResponseEntity.ok(fileStorageService.uploadFile(file));
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/download/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(
      @PathVariable String fileName, HttpServletRequest request) {
    try {
      Resource resource = fileStorageService.loadFileAsResource(fileName);
      String contentType =
          request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + resource.getFilename() + "\"")
          .body(resource);
    } catch (Exception ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<String>> listFiles() throws IOException {
    List<String> fileNames =
        Files.list(fileStorageLocation).map(Path::getFileName).map(Path::toString).toList();

    return ResponseEntity.ok(fileNames);
  }
}
