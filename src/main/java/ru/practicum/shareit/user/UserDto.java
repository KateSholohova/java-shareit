package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String name;
    @Email
    private String email;
}
