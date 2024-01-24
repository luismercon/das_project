package pt.isec.mei.das.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.isec.mei.das.entity.ProjectLanguage;

import java.util.Optional;

public interface ProjectLanguageRepository extends JpaRepository<ProjectLanguage, Long> {

  @Query(
      value =
          """
                      select pl
                      from ProjectLanguage pl
                      where pl.fileExtension = :fileExtension
                  """)
  Optional<ProjectLanguage> findByFileExtension(String fileExtension);
}
