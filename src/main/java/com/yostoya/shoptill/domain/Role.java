package com.yostoya.shoptill.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


public class Role extends BaseDomain{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType name;

    @Column(nullable = false)
    private String permissions;
}
