package com.certimetergroup.easycv.domainapi.model;

import com.certimetergroup.easycv.commons.enums.DomainTypeEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity @Table(name = "domains")
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private DomainTypeEnum type;

    private Long maxGrade;
}
