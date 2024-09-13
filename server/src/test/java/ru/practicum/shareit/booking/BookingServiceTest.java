import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/testt"})
@SpringJUnitConfig({ShareItServer.class, ItemService.class, UserService.class, BookingService.class})
class BookingServiceTest {

    private final EntityManager em;

    private final ItemService itemService;

    private final UserService userService;

    private final BookingService bookingService;


    @Test
    void getByBookerId() {
        UserDto userDto = makeUserDto("Иван Иванович", "ii@mail.ru");
        UserDto userDto1 = makeUserDto("Петр Петрович", "pp@mail.ru");
        ItemDto itemDto1 = makeItemDto("Item1 Name", "Item1 Description", true);
        UserDto userDto2 = userService.create(userDto);
        UserDto userDto3 = userService.create(userDto1);
        ItemDto itemDto = itemService.create(userDto2.getId(), itemDto1);
        BookingDto bookingDto = makeBookingDto(itemDto.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        bookingService.create(userDto3.getId(), bookingDto);
        List<BookingDto> bookingsDto = bookingService.getByBookerId(userDto3.getId(), State.ALL);
        assertEquals(1, bookingsDto.size());
        assertEquals(bookingDto.getStart(), bookingsDto.get(0).getStart());
        assertEquals(bookingDto.getEnd(), bookingsDto.get(0).getEnd());
        assertEquals(bookingsDto.get(0).getItem().getId(), itemDto.getId());

    }

    @Test
    void getByOwnerId() {
        UserDto userDto = makeUserDto("Иван Иванович", "ii@mail.ru");
        UserDto userDto1 = makeUserDto("Петр Петрович", "pp@mail.ru");
        ItemDto itemDto1 = makeItemDto("Item1 Name", "Item1 Description", true);
        UserDto userDto2 = userService.create(userDto);
        UserDto userDto3 = userService.create(userDto1);
        ItemDto itemDto = itemService.create(userDto2.getId(), itemDto1);
        BookingDto bookingDto = makeBookingDto(itemDto.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        bookingService.create(userDto3.getId(), bookingDto);
        log.info("users {}", userService.findAll());
        List<BookingDto> bookingsDto = bookingService.getByOwnerId(userDto2.getId(), State.ALL);
        assertEquals(1, bookingsDto.size());
        assertEquals(bookingDto.getStart(), bookingsDto.get(0).getStart());
        assertEquals(bookingDto.getEnd(), bookingsDto.get(0).getEnd());
        assertEquals(bookingsDto.get(0).getItem().getId(), itemDto.getId());
    }


    private UserDto makeUserDto(String email, String name) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setName(name);

        return dto;
    }

    private ItemDto makeItemDto(String name, String description, boolean available) {
        ItemDto dto = new ItemDto();
        dto.setDescription(description);
        dto.setName(name);
        dto.setAvailable(available);
        return dto;
    }

    private BookingDto makeBookingDto(int itemId, LocalDateTime start, LocalDateTime end) {
        BookingDto dto = new BookingDto();
        dto.setItemId(itemId);
        dto.setStart(start);
        dto.setEnd(end);
        return dto;
    }
}