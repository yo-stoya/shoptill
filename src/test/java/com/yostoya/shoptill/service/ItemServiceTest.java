package com.yostoya.shoptill.service;

import com.yostoya.shoptill.domain.Item;
import com.yostoya.shoptill.domain.dto.ItemDto;
import com.yostoya.shoptill.mapper.ItemMapper;
import com.yostoya.shoptill.repository.ItemRepository;
import com.yostoya.shoptill.service.impl.ItemServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @Mock
    private ItemMapper mapper;

    @InjectMocks
    private ItemServiceImpl service;

    @Test
    void addItem() {

        final Item item = new Item("item", BigDecimal.TEN, false);
        setField(item, "id", 1L);

        final ItemDto createDto = new ItemDto(null, "item", BigDecimal.TEN, false);
        final ItemDto expected = new ItemDto(1L, "item", BigDecimal.TEN, false);

        when(mapper.toDto(repository.save(any()))).thenReturn(expected);

        final ItemDto actual = service.addItem(createDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update() {

        final Item item = new Item("item", BigDecimal.TEN, false);
        setField(item, "id", 1L);

        final ItemDto updateDto = new ItemDto(null, "update", BigDecimal.TEN, false);
        final ItemDto expected = new ItemDto(1L, "update", BigDecimal.TEN, false);


        when(repository.findById(any())).thenReturn(Optional.of(item));
        when(mapper.toDto(repository.save(mapper.update(any(), any())))).thenReturn(expected);

        final ItemDto actual = service.update(1L, updateDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void delete() {

        long id = 1L;

        willDoNothing().given(repository).deleteById(id);
        service.delete(id);
        verify(repository, times(1)).deleteById(id);
    }
}