package com.certimetergroup.talentos.domainapi.repository;

import com.certimetergroup.talentos.domainapi.model.DomainOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainOptionRepository extends JpaRepository<DomainOption, Long> {
}