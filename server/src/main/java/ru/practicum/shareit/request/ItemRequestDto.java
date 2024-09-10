package ru.practicum.shareit.request;


import lombok.Data;
import ru.practicum.shareit.item.ItemFotRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private int id;
    private String description;
    private LocalDateTime created;
    private List<ItemFotRequest> items;
}
