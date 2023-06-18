package com.yostoya.shoptill.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yostoya.shoptill.domain.RoleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


public record RoleDto(

        @NotBlank
        String name,

        @NotBlank
        String permissions
) {

}
