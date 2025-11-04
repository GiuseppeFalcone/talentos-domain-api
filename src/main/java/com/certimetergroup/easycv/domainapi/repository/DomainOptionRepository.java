package com.certimetergroup.easycv.domainapi.repository;

import com.certimetergroup.easycv.domainapi.model.DomainOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainOptionRepository extends JpaRepository<DomainOption, Long> {
}
