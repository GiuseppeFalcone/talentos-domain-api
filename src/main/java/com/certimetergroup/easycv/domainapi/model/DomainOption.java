package com.certimetergroup.easycv.domainapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domain_option")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DomainOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domainName;

    @Column(name = "value", nullable = false)
    private String value;
}
