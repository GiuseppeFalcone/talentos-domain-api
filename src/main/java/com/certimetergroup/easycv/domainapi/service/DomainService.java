package com.certimetergroup.easycv.domainapi.service;

import com.certimetergroup.easycv.commons.enums.ResponseEnum;
import com.certimetergroup.easycv.commons.exception.ServerSideError;
import com.certimetergroup.easycv.domainapi.dto.DTOCreateDomain;
import com.certimetergroup.easycv.domainapi.dto.DTODomain;
import com.certimetergroup.easycv.domainapi.exception.DomainNotFoundException;
import com.certimetergroup.easycv.domainapi.mapper.DomainMapper;
import com.certimetergroup.easycv.domainapi.model.Domain;
import com.certimetergroup.easycv.domainapi.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service @Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DomainService {

    private final DomainRepository domainRepository;
    private final DomainMapper domainMapper;

    public PagedModel<DTODomain> getAllDomains(Integer page, Integer pageSize) {
        Pageable paging = PageRequest.of(page, pageSize);
        return new PagedModel<>(domainRepository.findAll(paging).map(domainMapper::toDTODomain));
    }

    public DTODomain getDomainById(Long id) {
        return (domainRepository.findById(id).map(domainMapper::toDTODomain)).orElseThrow(new DomainNotFoundException(id).get());
    }

    public DTODomain addNewDomain(DTOCreateDomain dtoCreateDomain) {
        try {
            return domainMapper.toDTODomain(
                    domainRepository.save(domainMapper.toDomainFromCreateDomain(dtoCreateDomain)));
        } catch (Exception _) {
            throw new ServerSideError(ResponseEnum.INTERNAL_SERVER_ERROR, "Error in saving new domain");
        }
    }

    public DTODomain updateWholeDomain(Long id, DTODomain dtoDomain) {
        if (!id.equals(dtoDomain.getId()))
            throw new IllegalArgumentException("Provided id in request url and user id in request body are NOT equal");
        try {
            Optional<Domain> optionalDomain = domainRepository.findById(id);
            if (optionalDomain.isEmpty())
                throw new DomainNotFoundException(id);
            Domain domain = optionalDomain.get();
            DomainMapper.updateFromDTO(dtoDomain, domain);
            return domainMapper.toDTODomain(domainRepository.save(domain));
        } catch (Exception _) {
            throw new ServerSideError(ResponseEnum.INTERNAL_SERVER_ERROR, "Error in updating Domain data");
        }
    }

    public void deleteDomain(Long id) {
        Domain domain = domainRepository.findById(id).orElseThrow(new DomainNotFoundException(id).get());
        try {
            domainRepository.delete(domain);
        } catch (Exception _) {
            throw new ServerSideError(ResponseEnum.INTERNAL_SERVER_ERROR, "Error in deleting domain");
        }
    }
}
