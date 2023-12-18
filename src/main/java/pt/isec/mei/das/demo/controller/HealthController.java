package pt.isec.mei.das.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/health")
public class HealthController {

  @GetMapping
  public ResponseEntity<String> checkHealth() {
    return ResponseEntity.ok("System running smoothly");
  }
}
