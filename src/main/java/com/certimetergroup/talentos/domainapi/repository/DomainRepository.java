package com.certimetergroup.talentos.domainapi.repository;

import com.certimetergroup.talentos.domainapi.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>, JpaSpecificationExecutor<Domain> {
    Optional<Domain> findByNameContainingIgnoreCase(String name);
}
