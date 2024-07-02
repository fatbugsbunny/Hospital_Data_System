package com.example.hospitalsystem.repositories;

import com.example.hospitalsystem.entities.AuditTrail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditTrailRepository extends CrudRepository<AuditTrail, Long> {
}
