package com.certimetergroup.talentos.domainapi.service;

import com.certimetergroup.talentos.commons.response.dto.domain.CreateDomainDto;
import com.certimetergroup.talentos.commons.response.dto.domain.DomainDto;
import com.certimetergroup.talentos.commons.enumeration.ResponseEnum;
import com.certimetergroup.talentos.commons.exception.FailureException;
import com.certimetergroup.talentos.domainapi.mapper.DomainMapper;
import com.certimetergroup.talentos.domainapi.mapper.DomainOptionMapper;
import com.certimetergroup.talentos.domainapi.model.Domain;
import com.certimetergroup.talentos.domainapi.model.DomainOption;
import com.certimetergroup.talentos.domainapi.repository.DomainRepository;
import com.certimetergroup.talentos.domainapi.repository.specification.DomainSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final DomainRepository domainRepository;
    private final DomainMapper domainMapper;
    private final DomainOptionMapper domainOptionMapper;

    public PagedModel<DomainDto> getDomains(Integer page, Integer pageSize, String domainName,
                                            String domainOptionValue) {
        Pageable paging = PageRequest.of(page - 1, pageSize);

        Specification<Domain> spec = Specification.unrestricted();

        if (StringUtils.hasText(domainName))
            spec = spec.and(DomainSpecification.containsDomainName(domainName));

        if (StringUtils.hasText(domainOptionValue))
            spec = spec.and(DomainSpecification.containsDomainOptionValue(domainOptionValue));

        Page<Domain> resultPage = domainRepository.findAll(spec, paging);

        Page<DomainDto> resultDtoPage = resultPage.map(domain -> {
            DomainDto dto = domainMapper.toDTO(domain);

            Stream<DomainOption> optionsStream = domain.getDomainOptions().stream();

            if (StringUtils.hasText(domainOptionValue)) {
                optionsStream = optionsStream.filter(opt ->
                        opt.getValue() != null &&
                                opt.getValue().toLowerCase().contains(domainOptionValue.toLowerCase())
                );
            }

            dto.setDomainOptions(optionsStream.map(domainOptionMapper::toDto).collect(Collectors.toSet()));
            return dto;
        });
        return new PagedModel<>(resultDtoPage);
    }

    public Optional<DomainDto> getDomain(Long domainId, Set<Long> domainOptionIds) {
        return domainRepository.findById(domainId).map(domain -> {
            DomainDto dto = domainMapper.toDTO(domain);
            if (domainOptionIds != null) {
                dto.setDomainOptions(domain.getDomainOptions().stream()
                        .filter(domainOption -> domainOptionIds.contains(domainOption.getId()))
                        .map(domainOptionMapper::toDto)
                        .collect(Collectors.toSet()));
            } else {
                dto.setDomainOptions(domain.getDomainOptions().stream().map(domainOptionMapper::toDto).collect(Collectors.toSet()));
            }
            return dto;
        });
    }

    public DomainDto addNewDomain(CreateDomainDto dtoCreateDomain) {
        Specification<Domain> spec = Specification.unrestricted();
        spec = spec.and(DomainSpecification.hasDomainName(dtoCreateDomain.getDomainName()));
        List<Domain> existingDomains = domainRepository.findAll(spec);

        if (!existingDomains.isEmpty())
            throw new FailureException(ResponseEnum.ALREADY_EXISTS, "Domain already exists with given name");

        Domain domain = domainMapper.toEntityFromCreateDto(dtoCreateDomain);
        Set<DomainOption> domainOptionSet = domainOptionMapper.toEntitySet(dtoCreateDomain.getDomainOptions(), domain);
        domain.setDomainOptions(domainOptionSet);

        return domainMapper.toDTO(domainRepository.save(domain));
    }

    @Transactional
    public Optional<DomainDto> replaceDomainData(Long domainId, DomainDto domainDto) {
        if (!domainId.equals(domainDto.getDomainId()))
            throw new FailureException(ResponseEnum.BAD_REQUEST, "DomainId in path variable and Body are not equal");
        Optional<Domain> optionalDomain = domainRepository.findById(domainId);
        if (optionalDomain.isEmpty())
            throw new FailureException(ResponseEnum.NOT_FOUND);
        Domain domain = optionalDomain.get();
        domainMapper.updateFromDTO(domainDto, domain);
        domainOptionMapper.synchronizeOptions(domainDto.getDomainOptions(), domain.getDomainOptions(), domain);
        return Optional.of(domainMapper.toDTO(domainRepository.save(domain)));
    }

    @Transactional
    public void deleteDomain(Long domainId) {
        domainRepository.deleteById(domainId);
    }
}
