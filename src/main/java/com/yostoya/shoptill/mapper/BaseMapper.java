package com.yostoya.shoptill.mapper;


import com.yostoya.shoptill.domain.Item;
import com.yostoya.shoptill.domain.Role;
import com.yostoya.shoptill.domain.dto.ItemDto;
import com.yostoya.shoptill.domain.dto.UserDto;
import com.yostoya.shoptill.exception.ApiException;
import com.yostoya.shoptill.repository.ItemRepository;
import com.yostoya.shoptill.repository.RoleRepository;
import com.yostoya.shoptill.security.UserPrincipal;
import com.yostoya.shoptill.service.facade.TillFacade;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MapperConfig;
import org.mapstruct.Named;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@MapperConfig
@RequiredArgsConstructor
public class BaseMapper {

    private final RoleRepository roleRepository;
    private final ItemRepository itemRepository;
    private final TillFacade tillFacade;
    private final ItemMapper itemMapper;

    private Role findRoleByUserId(final Long id) {
        return roleRepository.findById(id).orElseThrow();
    }

    @Named("findRole")
    public String findRole(final Long id) {
        return findRoleByUserId(id).getName().name();
    }

    @Named("findPermissions")
    public String findPermissions(final Long id) {
        return findRoleByUserId(id).getPermissions();
    }

    @Named("findItemById")
    public Item findItemById(final Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ApiException("Item not found"));
    }

    @Named("findItemByName")
    public Item findItemByName(final String name) {
        final Item item = itemRepository.findByName(name).orElseThrow(() -> new ApiException("Item not found"));
        return new Item(item.getName(), item.getPrice(), item.isHalfPrice());
    }


    @Named("findItems")
    private List<ItemDto> findItems(final Collection<String> names) {

        final List<Item> items = names.stream().map(s -> {
            final Item item = itemRepository.findByName(s).get();
            return new Item(item.getName(), item.getPrice(), item.isHalfPrice());
        }).toList();

        return items.stream().map(itemMapper::toDto).toList();
    }




    @Named("getTotalAmount")
    public BigDecimal getTotalAmount(final Collection<String> names) {

        final List<Item> items = names.stream().map(s -> {
            final Item item = itemRepository.findByName(s).get();
            return new Item(item.getName(), item.getPrice(), item.isHalfPrice());
        }).toList();

        return tillFacade.calcTotal(items);
    }

}
