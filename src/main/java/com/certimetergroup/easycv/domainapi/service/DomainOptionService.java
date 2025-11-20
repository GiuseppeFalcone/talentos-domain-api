package com.certimetergroup.easycv.domainapi.service;

import com.certimetergroup.easycv.commons.response.dto.domain.DomainOptionDto;
import com.certimetergroup.easycv.domainapi.mapper.DomainOptionMapper;
import com.certimetergroup.easycv.domainapi.repository.DomainOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomainOptionService {

    private final DomainOptionRepository domainOptionRepository;
    private final DomainOptionMapper domainOptionMapper;

    public Optional<DomainOptionDto> getDomainOption(Long domainOptionId) {
        return domainOptionRepository.findById(domainOptionId).map(domainOptionMapper::toDto);
    }
}
