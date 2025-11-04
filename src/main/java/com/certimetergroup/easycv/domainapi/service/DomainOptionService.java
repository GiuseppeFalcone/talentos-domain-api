package com.certimetergroup.easycv.domainapi.service;

import com.certimetergroup.easycv.domainapi.repository.DomainOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DomainOptionService {
    private final DomainOptionRepository domainOptionRepository;
}
