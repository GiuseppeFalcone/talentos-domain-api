package com.certimetergroup.easycv.domainapi.controller;

import com.certimetergroup.easycv.commons.dto.domain.CreateDomainDto;
import com.certimetergroup.easycv.commons.enumeration.ResponseEnum;
import com.certimetergroup.easycv.commons.dto.Response;
import com.certimetergroup.easycv.commons.dto.domain.DomainDto;
import com.certimetergroup.easycv.domainapi.service.DomainService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/domains")
@Tag(name = "Domains", description = "Operations on domain data")
@Validated
@RequiredArgsConstructor
public class DomainController {
    private final DomainService domainService;

    @GetMapping
    public ResponseEntity<Response<PagedModel<DomainDto>>> getDomains(
            @RequestParam(defaultValue = "1") @Positive(message = "Page must be > 0") Integer page,
            @RequestParam(defaultValue = "5") @Positive(message = "Page size must be > 0") Integer pageSize,
            @RequestParam(required = false) String domainName) {
        return ResponseEntity.ok().body(new Response<>(ResponseEnum.SUCCESS, domainService.getDomains(page, pageSize, domainName)));
    }

    @GetMapping("/{domainId}")
    public ResponseEntity<Response<DomainDto>> getDomain(
            @PathVariable @NotNull(message = "Domain Id required") @Positive(message = "Domain Id must be > 0") Long domainId) {
        Optional<DomainDto> optionalDomainDto = domainService.getDomain(domainId);
        return optionalDomainDto.map(
                domainDto -> ResponseEntity.ok().body(new Response<>(ResponseEnum.SUCCESS, domainDto)))
                .orElseGet(() -> ResponseEntity.status(ResponseEnum.NOT_FOUND.httpStatus).body(new Response<>(ResponseEnum.NOT_FOUND)));
    }

    @PostMapping
    public ResponseEntity<Response<DomainDto>> addNewDomain(@RequestBody @NotNull(message = "CreateDomainDto required") CreateDomainDto createDomainDto) {
        return ResponseEntity.ok().body(new Response<>(ResponseEnum.SUCCESS, domainService.addNewDomain(createDomainDto)));
    }

    @PutMapping("/{domainId}")
    public ResponseEntity<Response<DomainDto>> replaceDomainData(
            @PathVariable @NotNull(message = "Domain Id required") @Positive(message = "Wrong domain id provided") Long domainId,
            @RequestBody @NotNull(message = "DomainDto required") DomainDto domainDto) {
        Optional<DomainDto> optionalDomainDto = domainService.replaceDomainData(domainId, domainDto);
        return optionalDomainDto.map(
                dto -> ResponseEntity.ok().body(new Response<>(ResponseEnum.SUCCESS, dto)))
                .orElseGet(() -> ResponseEntity.status(ResponseEnum.NOT_FOUND.httpStatus).body(new Response<>(ResponseEnum.NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteDomain(
            @PathVariable @NotNull @Positive(message = "Wrong domain id provided") Long id) {
        domainService.deleteDomain(id);
        return ResponseEntity.ok().body(new Response<>(ResponseEnum.SUCCESS));
    }
}
