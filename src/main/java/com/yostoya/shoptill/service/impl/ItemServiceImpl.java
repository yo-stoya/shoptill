package com.yostoya.shoptill.service.impl;

import com.yostoya.shoptill.domain.Item;
import com.yostoya.shoptill.domain.dto.ItemDto;
import com.yostoya.shoptill.exception.ApiException;
import com.yostoya.shoptill.mapper.ItemMapper;
import com.yostoya.shoptill.repository.ItemRepository;
import com.yostoya.shoptill.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addItem(final ItemDto newItem) {
        log.info("Adding item..");
        return itemMapper.toDto(itemRepository.save(itemMapper.toItem(newItem)));
    }

    @Override
    public ItemDto update(final Long id, final ItemDto updateItem) {
        log.info("Updating item..");
        return itemMapper.toDto(itemRepository.save(itemMapper.update(updateItem, getItem(id))));
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
        log.info("Item deleted");
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ApiException("Item not found"));
    }
}
