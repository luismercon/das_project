package pt.isec.mei.das.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PROJECT_LANGUAGE")
public class ProjectLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_language_id")
    private long id;

    @Column(name = "file_extension")
    private String file_extension;

    @Column(name = "programming_language")
    private String programming_language;
}
