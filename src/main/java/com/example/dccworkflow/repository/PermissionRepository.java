package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>,
        QuerydslPredicateExecutor<Permission> {
}
