package com.certimetergroup.easycv.domainapi.repository;

import com.certimetergroup.easycv.domainapi.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DomainRepository extends JpaRepository<Domain, Long> {
}
