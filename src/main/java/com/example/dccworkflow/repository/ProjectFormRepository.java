package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.ProjectForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFormRepository extends JpaRepository<ProjectForm, Long> {
}
