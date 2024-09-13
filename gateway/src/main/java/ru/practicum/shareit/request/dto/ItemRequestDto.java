package ru.practicum.shareit.request.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemFotRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private int id;
    @NotNull
    private String description;
    private LocalDateTime created;
    private List<ItemFotRequest> items;
}
