package com.certimetergroup.easycv.domainapi.exception;

import com.certimetergroup.easycv.commons.enums.ResponseEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter @Setter
public class DomainNotFoundException extends RuntimeException implements Supplier<DomainNotFoundException> {
    private ResponseEnum responseEnum;
    private String message;

    public DomainNotFoundException(Long id) {
        this.responseEnum = ResponseEnum.RESOURCE_NOT_FOUND;
        this.message = "No domain found with given id: " + id.toString();
    }

    @Override
    public DomainNotFoundException get() {
        return this;
    }
}
