package ru.practicum.shareit.request.dto;


import lombok.Data;
import ru.practicum.shareit.item.dto.ItemFotRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private int id;
    private String description;
    private LocalDateTime created;
    private List<ItemFotRequest> items;
}
