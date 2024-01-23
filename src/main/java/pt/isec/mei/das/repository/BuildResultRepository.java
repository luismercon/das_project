package pt.isec.mei.das.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.entity.Project;

import java.util.List;

@Repository
public interface BuildResultRepository extends JpaRepository<BuildResult, Long> {

    List<BuildResult> findBuildResultByProject(Project project);
}
