package com.yostoya.shoptill.mapper;

import com.yostoya.shoptill.domain.User;
import com.yostoya.shoptill.domain.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BaseMapper.class)
public interface UserMapper {

    @Mapping(target = "role.name", source = "id", qualifiedByName = "findRole")
    @Mapping(target = "role.permissions", source = "id", qualifiedByName = "findPermissions")
    UserDto toDto(User user);

}
