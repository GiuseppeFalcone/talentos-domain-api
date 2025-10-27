package com.certimetergroup.easycv.domainapi.controller;

import com.certimetergroup.easycv.commons.enums.ResponseEnum;
import com.certimetergroup.easycv.commons.response.PayloadResponse;
import com.certimetergroup.easycv.commons.response.Response;
import com.certimetergroup.easycv.domainapi.dto.DTOCreateDomain;
import com.certimetergroup.easycv.domainapi.dto.DTODomain;
import com.certimetergroup.easycv.domainapi.service.DomainService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController @RequestMapping("/domains")
@Tag(name = "Domains", description = "Operations on domains")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DomainController {

    private final DomainService domainService;

    // todo: implementare query functionalities
    // todo: implementare authentication
    // todo: implementare authorization
    @GetMapping
    public ResponseEntity<Response> getAllDomains(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") @Positive Integer pageSize) {
        return ResponseEntity.ok().body(
                new PayloadResponse<>(
                        ResponseEnum.SUCCESS,
                        domainService.getAllDomains(page, pageSize)
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSingleDomain(
            @PathVariable @NotNull @Positive(message = "wrong domain id") Long id) {
        return ResponseEntity.ok().body(
                new PayloadResponse<>(
                        ResponseEnum.SUCCESS,
                        domainService.getDomainById(id)));
    }

    @PostMapping
    public ResponseEntity<Response> addNewDomain(@RequestBody @NotNull @Valid DTOCreateDomain dtoCreateDomain) {
        return ResponseEntity.ok().body(
                new PayloadResponse<>(
                        ResponseEnum.SUCCESS,
                        domainService.addNewDomain(dtoCreateDomain)
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateWholeDomain(
            @PathVariable @NotNull @Positive(message = "Wrong domain id provided") Long id,
            @RequestBody @NotNull @Valid DTODomain dtoDomain) {
        return ResponseEntity.ok().body(
                new PayloadResponse<>(
                        ResponseEnum.SUCCESS,
                        domainService.updateWholeDomain(id, dtoDomain)
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteDomain(
            @PathVariable @NotNull @Positive(message = "Wrong domain id provided") Long id) {
        domainService.deleteDomain(id);
        return ResponseEntity.ok().body(
                new Response(ResponseEnum.SUCCESS)
        );
    }
}
