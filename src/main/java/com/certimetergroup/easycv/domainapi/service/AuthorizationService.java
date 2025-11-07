package com.certimetergroup.easycv.domainapi.service;

import com.certimetergroup.easycv.commons.enumeration.ResponseEnum;
import com.certimetergroup.easycv.commons.enumeration.UserRoleEnum;
import com.certimetergroup.easycv.commons.exception.FailureException;
import com.certimetergroup.easycv.domainapi.context.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final RequestContext requestContext;

    public void checkAuthorization() {
        boolean isAdmin = requestContext.getUserRole().equals(UserRoleEnum.ADMIN);
        boolean isSuperAdmin = requestContext.getUserRole().equals(UserRoleEnum.SUPERADMIN);
        if (!(isAdmin || isSuperAdmin))
            throw new FailureException(ResponseEnum.UNAUTHORIZED);
    }

}
