package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long>,
        QuerydslPredicateExecutor<ProjectFile> {
}
