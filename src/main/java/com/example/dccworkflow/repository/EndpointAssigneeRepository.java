package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.EndpointAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointAssigneeRepository extends JpaRepository<EndpointAssignee, Long> {
}
