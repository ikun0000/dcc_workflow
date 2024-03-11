package com.example.dccworkflow.repository;

import com.example.dccworkflow.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>,
        QuerydslPredicateExecutor<Client> {
}
