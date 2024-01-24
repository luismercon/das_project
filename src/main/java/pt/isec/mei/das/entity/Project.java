package pt.isec.mei.das.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PROJECT")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "project_id")
  private long id;

  @OneToOne
  @JoinColumn(name = "project_language_id", nullable = false)
  private ProjectLanguage projectLanguage;

  @Column(name = "name")
  private String name;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "file_path")
  private String filePath;

  @OneToMany(mappedBy = "project")
  private List<BuildResult> buildResults;
}
