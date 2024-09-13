import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.BookingDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;


@JsonTest
@Slf4j
@SpringJUnitConfig({BookingDto.class})
public class BookingJsonTest {

    @Autowired
    JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1);
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 1, 1, 1));
        bookingDto.setEnd(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        JsonContent<BookingDto> result = json.write(bookingDto);
        assertTrue(result.getJson().contains("\"id\":1"));
        assertTrue(result.getJson().contains("\"start\":\"2024-01-01T01:01:01\""));
        assertTrue(result.getJson().contains("\"end\":\"2025-01-01T01:01:01\""));

    }
}
