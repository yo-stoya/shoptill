package com.yostoya.shoptill.web;


import com.yostoya.shoptill.domain.HttpResponse;
import com.yostoya.shoptill.domain.dto.ItemDto;
import com.yostoya.shoptill.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/item")
public class ItemController {

    private final ItemService itemService;

    @PreAuthorize("hasAuthority('CREATE:ITEM')")
    @PostMapping("/add")
    public ResponseEntity<HttpResponse> addItem(@RequestBody @Valid final ItemDto newItem) {

        final ItemDto created = itemService.addItem(newItem);
        return ResponseEntity
                .created(getCreatedUri(created.getId()))
                .body(new HttpResponse(CREATED, "Item added", Map.of("item", created)));

    }

    @PreAuthorize("hasAuthority('UPDATE:ITEM')")
    @PutMapping("/{id}/edit")
    public ResponseEntity<HttpResponse> update(@PathVariable final Long id,
                                               @RequestBody @Valid final ItemDto updateItem) {

        final ItemDto updated = itemService.update(id, updateItem);
        return ok(new HttpResponse(OK, "Item updated.", Map.of("item", updated)));

    }

    @PreAuthorize("hasAuthority('DELETE:ITEM')")
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<HttpResponse> delete(@PathVariable final Long id) {

        itemService.delete(id);
        return noContent().build();
    }

    private URI getCreatedUri(final Long id) {

        return URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/item/get/{id}")
                .build(id)
                .toString()
        );

    }
}
