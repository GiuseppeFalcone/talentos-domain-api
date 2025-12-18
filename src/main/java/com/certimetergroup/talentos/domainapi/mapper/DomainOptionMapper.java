package com.certimetergroup.talentos.domainapi.mapper;

import com.certimetergroup.talentos.commons.response.dto.domain.DomainOptionDto;
import com.certimetergroup.talentos.domainapi.model.Domain;
import com.certimetergroup.talentos.domainapi.model.DomainOption;

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

    @Mapping(target = "domainOptionId", source = "id")
    DomainOptionDto toDto(DomainOption domainOption);

    default Set<DomainOption> toEntitySet(Set<String> values, Domain domain) {
        if (values == null || values.isEmpty()) {
            return Collections.emptySet();
        }
        return values.stream()
                .filter(Objects::nonNull)
                .map(value -> this.toEntity(value, domain))
                .collect(Collectors.toSet());
    }

    default void synchronizeOptions(Set<DomainOptionDto> domainOptionDtos,
                                        @MappingTarget Set<DomainOption> currentOptions,
                                        Domain parentDomain) {

        Set<String> desired = (domainOptionDtos != null && !domainOptionDtos.isEmpty()) ?
                domainOptionDtos.stream().map(DomainOptionDto::getValue).collect(Collectors.toSet()) : new HashSet<>();

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
