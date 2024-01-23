package pt.isec.mei.das.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

  @Column(name = "name")
  private String name;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "file_path")
  private String filePath;

  @OneToMany(mappedBy = "project")
  private List<BuildResult> buildResults;
}
