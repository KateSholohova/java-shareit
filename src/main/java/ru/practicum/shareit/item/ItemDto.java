package ru.practicum.shareit.item;

import lombok.Data;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer idItemRequest;
}
