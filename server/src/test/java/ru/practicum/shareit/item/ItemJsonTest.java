import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.ItemDto;

import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
@Slf4j
@SpringJUnitConfig({ItemDto.class})
public class ItemJsonTest {
    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setDescription("Item Description");
        itemDto.setName("Item Name");
        itemDto.setAvailable(true);
        JsonContent<ItemDto> result = json.write(itemDto);
        assertTrue(result.getJson().contains("\"id\":1"));
        assertTrue(result.getJson().contains("\"description\":\"Item Description\""));
        assertTrue(result.getJson().contains("\"name\":\"Item Name\""));
        assertTrue(result.getJson().contains("\"available\":true"));

    }

}
