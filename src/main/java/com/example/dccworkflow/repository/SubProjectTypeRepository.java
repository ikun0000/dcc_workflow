package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.SubProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubProjectTypeRepository extends JpaRepository<SubProjectType, Long> {
}
