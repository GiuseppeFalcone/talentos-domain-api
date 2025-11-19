package com.certimetergroup.easycv.domainapi.repository.specification;

import com.certimetergroup.easycv.domainapi.model.Domain;
import com.certimetergroup.easycv.domainapi.model.DomainOption;
import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainSpecification {

    public static Specification<Domain> containsDomainName(String domainName) {
        return (root, query, cb) -> (
            cb.like(cb.lower(root.get("name")), "%" + domainName.toLowerCase() + "%")
        );
    }

    public static Specification<Domain> containsDomainOptionValue(String domainOptionValue) {
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            Join<Domain, DomainOption> optionJoin = root.join("domainOptions");
            return cb.like(cb.lower(optionJoin.get("value")), "%" + domainOptionValue.toLowerCase() + "%");
        };
    }
}
