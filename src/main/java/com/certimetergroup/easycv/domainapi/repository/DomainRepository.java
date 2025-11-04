package com.certimetergroup.easycv.domainapi.repository;

import com.certimetergroup.easycv.domainapi.model.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
    Optional<Domain> findByNameIgnoreCase(String name);

    Page<Domain> findAllByNameContainingIgnoreCase(Pageable paging, String domainName);
}
