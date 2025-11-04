package com.certimetergroup.easycv.domainapi.service;

import com.certimetergroup.easycv.commons.dto.domain.CreateDomainDto;
import com.certimetergroup.easycv.commons.dto.domain.DomainDto;
import com.certimetergroup.easycv.commons.enumeration.ResponseEnum;
import com.certimetergroup.easycv.commons.exception.FailureException;
import com.certimetergroup.easycv.domainapi.mapper.DomainMapper;
import com.certimetergroup.easycv.domainapi.mapper.DomainOptionMapper;
import com.certimetergroup.easycv.domainapi.model.Domain;
import com.certimetergroup.easycv.domainapi.model.DomainOption;
import com.certimetergroup.easycv.domainapi.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final DomainRepository domainRepository;
    private final DomainMapper domainMapper;
    private final DomainOptionMapper domainOptionMapper;

    public PagedModel<DomainDto> getDomains(Integer page, Integer pageSize, String domainName) {
        Pageable paging = PageRequest.of(page - 1, pageSize);
        Page<Domain> resultPage;
        if (domainName != null && !domainName.isBlank())
            resultPage = domainRepository.findAllByNameContainingIgnoreCase(paging, domainName);
        else resultPage = domainRepository.findAll(paging);
        Page<DomainDto> resultDtoPage = resultPage.map(domain -> {
            DomainDto dto = domainMapper.toDTO(domain);
            dto.setDomainOptions(domainOptionMapper.optionsToStrings(domain.getDomainOptions()));
            return dto;
        });
        return new PagedModel<>(resultDtoPage);
    }

    public Optional<DomainDto> getDomain(Long domainId) {
        return domainRepository.findById(domainId).map(domain -> {
            DomainDto dto = domainMapper.toDTO(domain);
            dto.setDomainOptions(domainOptionMapper.optionsToStrings(domain.getDomainOptions()));
            return dto;
        });
    }

    public DomainDto addNewDomain(CreateDomainDto dtoCreateDomain) {
        Optional<Domain> optionalDomain = domainRepository.findByNameIgnoreCase(dtoCreateDomain.getDomainName());
        if (optionalDomain.isEmpty())
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
