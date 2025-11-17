package com.certimetergroup.easycv.domainapi.middleware;

import com.certimetergroup.easycv.commons.enumeration.ResponseEnum;
import com.certimetergroup.easycv.commons.enumeration.UserRoleEnum;
import com.certimetergroup.easycv.commons.exception.FailureException;
import com.certimetergroup.easycv.commons.response.dto.user.UserDto;
import com.certimetergroup.easycv.commons.utility.HttpHeaderUtil;
import com.certimetergroup.easycv.domainapi.context.RequestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Order(1)
@Component
@RequiredArgsConstructor
public class JwtClaimExtractor extends OncePerRequestFilter {
    private final RequestContext requestContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String accessToken = HttpHeaderUtil.sanitizeAccessToken(request.getHeader("Authorization"));

            String[] accessTokenSplit = accessToken.split("\\.");

            byte[] payloadBytes = Base64.getUrlDecoder().decode(accessTokenSplit[1]);

            String payload = new String(payloadBytes, StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> claimsMap = mapper.readValue(payload, Map.class);

            requestContext.setUserRole(UserRoleEnum.valueOf((String) claimsMap.get("role")));
            requestContext.setUserId(Long.parseLong((String) claimsMap.get("sub")));

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            throw new FailureException(ResponseEnum.JWT_MALFORMED, exception);
        }
    }
}
