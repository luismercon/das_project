package pt.isec.mei.das.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "BUILD_RESULT")
public class BuildResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "build_result_id")
  private long id;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @Column(name = "compilation_time_ms")
  private Long compilationTimeInMs;

  @Column(name = "compilation_status")
  private String compilationStatus;

  @Column(name = "executable_file_path")
  private String executableFilePath;

  @Column(name = "build_logs")
  private String buildLogs;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;

  @Column(name = "notification_needed")
  private boolean notificationNeeded;
}
