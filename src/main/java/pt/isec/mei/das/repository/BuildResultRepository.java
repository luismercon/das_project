package pt.isec.mei.das.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.entity.Project;

@Repository
public interface BuildResultRepository extends JpaRepository<BuildResult, Long> {

  List<BuildResult> findBuildResultByProject(Project project);

  @Query(
      value =
          """
          select b.compilationStatus from BuildResult b where b.id = :id
          """)
  String findStatusById(Long id);
}
