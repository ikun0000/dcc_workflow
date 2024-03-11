package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SerialNumberRepository extends JpaRepository<SerialNumber, Long>,
        QuerydslPredicateExecutor<SerialNumber> {
}
