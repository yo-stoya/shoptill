package com.yostoya.shoptill.repository;

import com.yostoya.shoptill.domain.Role;
import com.yostoya.shoptill.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType roleType);
}
