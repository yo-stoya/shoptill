package com.yostoya.shoptill.service;

import com.yostoya.shoptill.domain.Item;
import com.yostoya.shoptill.domain.Role;
import com.yostoya.shoptill.domain.RoleType;
import com.yostoya.shoptill.domain.User;
import com.yostoya.shoptill.domain.dto.*;
import com.yostoya.shoptill.exception.ApiException;
import com.yostoya.shoptill.mapper.ItemMapper;
import com.yostoya.shoptill.mapper.UserMapper;
import com.yostoya.shoptill.repository.ItemRepository;
import com.yostoya.shoptill.repository.RoleRepository;
import com.yostoya.shoptill.repository.UserRepository;
import com.yostoya.shoptill.service.facade.TillFacade;
import com.yostoya.shoptill.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  UserMapper userMapper;
    @Mock
    private  RoleRepository roleRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  ItemRepository itemRepository;
    @Mock
    private  ItemMapper itemMapper;
    @Mock
    private  TillFacade tillFacade;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void register() {
        final User user = new User(null, "f", "l", "e", "p", Mockito.mock(Role.class), LocalDateTime.now());

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        final Role role = new Role(RoleType.ROLE_USER, "P");
        when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(role);

        final UserDto expected = new UserDto(1L, "f", "l", "e", Mockito.mock(RoleDto.class), LocalDateTime.now());
        when(userMapper.toDto(user)).thenReturn(expected);

        String encodedPassword = "p";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);

        final UserDto actual = service.register(user);

        assertNotNull(actual);
        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(roleRepository, times(1)).findByName(RoleType.ROLE_USER);
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void register_user_exist_exception() {

        when(userRepository.existsByEmail(any())).thenThrow(ApiException.class);
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> service.register(Mockito.mock(User.class)));
    }

    @Test
    void getUserByEmail() {
        final User user = new User(null, "f", "l", "e", "p", Mockito.mock(Role.class), LocalDateTime.now());
        final UserDto expected = new UserDto(1L, "f", "l", "e", Mockito.mock(RoleDto.class), LocalDateTime.now());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(userMapper.toDto(userRepository.findByEmail(any()).get())).thenReturn(expected);

        final UserDto actual = service.getUserByEmail(any());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getUserByEmail_notFound_exception() {

        when(userRepository.findByEmail(any())).thenThrow(ApiException.class);
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> service.getUserByEmail(any()));
    }

    @Test
    void tillCheckout() {
        final List<String> itemNames = List.of("i1", "i2", "i3");
        NewOrderDto orderDto = new NewOrderDto(itemNames);

        Item item1 = new Item("i1", new BigDecimal("10.00"), false);
        Item item2 = new Item("i2", new BigDecimal("15.00"), true);
        Item item3 = new Item("i3", new BigDecimal("20.00"), false);
        when(itemRepository.findByName("i1")).thenReturn(Optional.of(item1));
        when(itemRepository.findByName("i2")).thenReturn(Optional.of(item2));
        when(itemRepository.findByName("i3")).thenReturn(Optional.of(item3));

        BigDecimal expectedTotal = new BigDecimal("35.00");
        when(tillFacade.calcTotal(anyList())).thenReturn(expectedTotal);

        ItemDto itemDto1 = new ItemDto(1L, "i1", new BigDecimal("10.00"), false);
        ItemDto itemDto2 = new ItemDto(2L, "i2", new BigDecimal("15.00"), true);
        ItemDto itemDto3 = new ItemDto(3L, "i3", new BigDecimal("20.00"), false);
        when(itemMapper.toDto(item1)).thenReturn(itemDto1);
        when(itemMapper.toDto(item2)).thenReturn(itemDto2);
        when(itemMapper.toDto(item3)).thenReturn(itemDto3);

        OrderDto result = service.tillCheckout(orderDto);

        assertEquals(itemNames.size(), result.getItems().size());
        assertEquals(expectedTotal + "aws", result.getTotalAmount());
        verify(itemRepository, times(itemNames.size())).findByName(anyString());
        verify(tillFacade, times(1)).calcTotal(anyList());
        verify(itemMapper, times(itemNames.size())).toDto(any(Item.class));
    }
}