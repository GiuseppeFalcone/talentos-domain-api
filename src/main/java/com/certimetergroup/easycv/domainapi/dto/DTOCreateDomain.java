package com.certimetergroup.easycv.domainapi.dto;

import com.certimetergroup.easycv.commons.enums.DomainTypeEnum;
import jakarta.validation.constraints.NotBlank;

public class DTOCreateDomain {

    @NotBlank(message = "Domain name required")
    private String name;

    @NotBlank(message = "Domain type required")
    private DomainTypeEnum type;

    private Long maxGrade;
}
