package com.yostoya.shoptill.service;

import com.yostoya.shoptill.domain.dto.OrderDto;
import com.yostoya.shoptill.domain.User;
import com.yostoya.shoptill.domain.dto.NewOrderDto;
import com.yostoya.shoptill.domain.dto.UserDto;

public interface UserService {

    UserDto register( final User user);

    UserDto getUserByEmail(String email);

    OrderDto tillCheckout(final NewOrderDto orderDto);
}
