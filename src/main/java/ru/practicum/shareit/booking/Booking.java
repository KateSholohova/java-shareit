package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
public class Booking {
    private int id;
    private Date start;
    private Date end;
    private Item item;
    private User booker;
    private Status status;
}
