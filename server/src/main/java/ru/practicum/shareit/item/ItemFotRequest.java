package ru.practicum.shareit.item;

import lombok.Data;

@Data
public class ItemFotRequest {
    private int id;
    private String name;
    private Integer ownerId;
}
