import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.user.UserDto;

import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
@Slf4j
@SpringJUnitConfig({UserDto.class})
public class UserJsonTest {
    @Autowired
    JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("User Name");
        userDto.setEmail("email@email.com");
        JsonContent<UserDto> result = json.write(userDto);
        assertTrue(result.getJson().contains("\"id\":1"));
        assertTrue(result.getJson().contains("\"name\":\"User Name\""));
        assertTrue(result.getJson().contains("\"email\":\"email@email.com\""));


    }
}
