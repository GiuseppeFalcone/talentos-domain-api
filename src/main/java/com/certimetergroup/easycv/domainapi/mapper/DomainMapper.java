package com.certimetergroup.easycv.domainapi.mapper;

import com.certimetergroup.easycv.domainapi.dto.DTOCreateDomain;
import com.certimetergroup.easycv.domainapi.dto.DTODomain;
import com.certimetergroup.easycv.domainapi.model.Domain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;

@Mapper(componentModel = "spring")
public interface DomainMapper {
    DTODomain toDTODomain(Domain domain);

    Domain toDomainFromCreateDomain (DTOCreateDomain dtoDomain);

    static void updateFromDTO (DTODomain dtoDomain, Domain domain) {
        BeanUtils.copyProperties(dtoDomain, domain);
    }
}
