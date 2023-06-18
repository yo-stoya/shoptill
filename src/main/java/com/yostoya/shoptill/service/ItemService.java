package com.yostoya.shoptill.service;

import com.yostoya.shoptill.domain.Item;
import com.yostoya.shoptill.domain.dto.ItemDto;

public interface ItemService {

    ItemDto addItem(final ItemDto newItem);

    ItemDto update(final Long id, final ItemDto updateItem);

    void delete(Long id);
}
