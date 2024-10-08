package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int id;
    private String text;
    private String authorName;
    private UserDto author;
    private ItemDto item;
    private LocalDateTime created;
}
