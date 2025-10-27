package com.certimetergroup.easycv.domainapi.dto;

import com.certimetergroup.easycv.commons.enums.DomainTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder @NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
@Getter
public class DTODomain {

    @Positive
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Domain name required")
    private String name;

    @NotBlank(message = "Domain type required")
    private DomainTypeEnum type;

    private Long maxGrade;
}
