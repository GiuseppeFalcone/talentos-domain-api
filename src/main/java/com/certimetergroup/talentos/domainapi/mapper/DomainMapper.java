package com.certimetergroup.talentos.domainapi.mapper;

import com.certimetergroup.talentos.commons.response.dto.domain.CreateDomainDto;
import com.certimetergroup.talentos.commons.response.dto.domain.DomainDto;
import com.certimetergroup.talentos.domainapi.model.Domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DomainMapper {

    @Mapping(target = "domainId", source = "id")
    @Mapping(target = "domainName", source = "name")
    @Mapping(target = "domainOptions", ignore = true)
    DomainDto toDTO(Domain domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "domainName")
    @Mapping(target = "domainOptions", ignore = true)
    Domain toEntityFromCreateDto(CreateDomainDto dtoCreateDomain);

    @Mapping(target = "name", source = "domainName")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "domainOptions", ignore = true)
    void updateFromDTO(DomainDto dto, @MappingTarget Domain domain);
}
