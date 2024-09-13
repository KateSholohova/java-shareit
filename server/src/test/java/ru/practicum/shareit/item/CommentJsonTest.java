import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.CommentDto;

import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
@Slf4j
@SpringJUnitConfig({CommentDto.class})
public class CommentJsonTest {
    @Autowired
    JacksonTester<CommentDto> json;

    @Test
    void testCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("Comment Text");
        JsonContent<CommentDto> result = json.write(commentDto);
        assertTrue(result.getJson().contains("\"id\":1"));
        assertTrue(result.getJson().contains("\"text\":\"Comment Text\""));

    }
}
