package ru.practicum.shareit.item;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer idItemRequest;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<Comment> comments;
}
