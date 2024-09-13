import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.request.ItemRequestDto;

import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
@Slf4j
@SpringJUnitConfig({ItemRequestDto.class})
public class RequestJsonTest {
    @Autowired
    JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setDescription("Request Description");
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertTrue(result.getJson().contains("\"id\":1"));
        assertTrue(result.getJson().contains("\"description\":\"Request Description\""));

    }
}
