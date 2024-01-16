package pt.isec.mei.das.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.isec.mei.das.entity.BuildResult;

public interface BuildResultRepository extends JpaRepository<BuildResult, Long> {}
