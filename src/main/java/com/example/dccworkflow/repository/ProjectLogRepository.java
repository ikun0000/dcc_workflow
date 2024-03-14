package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.ProjectLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectLogRepository extends JpaRepository<ProjectLog, Long>, QuerydslPredicateExecutor<ProjectLog> {
}
