package pt.isec.mei.das.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.isec.mei.das.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {}
