package com.certimetergroup.easycv.domainapi.mapper;

import com.certimetergroup.easycv.domainapi.model.Domain;
import com.certimetergroup.easycv.domainapi.model.DomainOption;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DomainOptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "domainName", source = "domain")
    @Mapping(target = "value", source = "value")
    DomainOption toEntity(String value, Domain domain);

    default Set<String> optionsToStrings(Set<DomainOption> domainOptionSet) {
        return domainOptionSet.stream().map(DomainOption::getValue).collect(Collectors.toSet());
    }

    default Set<DomainOption> toEntitySet(Set<String> values, Domain domain) {
        if (values == null || values.isEmpty()) {
            return Collections.emptySet();
        }
        return values.stream()
                .filter(Objects::nonNull)
                .map(value -> this.toEntity(value, domain))
                .collect(Collectors.toSet());
    }

    default Set<String> optionsToStrings(Set<DomainOption> domainOptionSet, Set<Long> domainOptionIds) {
        if (domainOptionSet == null) {
            return Collections.emptySet();
        }
        if (domainOptionIds == null || domainOptionIds.isEmpty()) {
            return optionsToStrings(domainOptionSet);
        }
        return domainOptionSet.stream()
                .filter(option -> domainOptionIds.contains(option.getId()))
                .map(DomainOption::getValue)
                .collect(Collectors.toSet());
    }

    default void synchronizeOptions(Set<String> desiredValues,
                                        @MappingTarget Set<DomainOption> currentOptions,
                                        Domain parentDomain) {
        Set<String> desired = (desiredValues != null) ? desiredValues : new HashSet<>();

        Set<String> originalValues = currentOptions.stream()
                .map(DomainOption::getValue)
                .collect(Collectors.toSet());

        currentOptions.removeIf(option -> !desired.contains(option.getValue()));

        for (String desiredValue : desired) {
            if (!originalValues.contains(desiredValue)) {
                currentOptions.add(this.toEntity(desiredValue, parentDomain));
            }
        }
    }
}
