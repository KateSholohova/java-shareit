package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemFotRequest {
    private int id;
    private String name;
    private Integer ownerId;
}
