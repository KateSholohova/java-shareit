import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/testt"})
@SpringJUnitConfig({ShareItServer.class, ItemService.class, UserService.class})
class ItemServiceTest {

    private final EntityManager em;

    private final ItemService itemService;

    private final UserService userService;

    @Test
    void findAll() {
        UserDto userDto = makeUserDto("Иван Иванович", "ii@mail.ru");
        UserDto userDto1 = makeUserDto("Петр Петрович", "pp@mail.ru");

        UserDto userDto2 = userService.create(userDto);
        UserDto userDto3 = userService.create(userDto1);
        ItemDto itemDto1 = makeItemDto("Item1 Name", "Item1 Description", true);
        ItemDto itemDto2 = makeItemDto("Item2 Name", "Item2 Description", true);
        ItemDto itemDto3 = makeItemDto("Item3 Name", "Item3 Description", true);
        ItemDto itemDto4 = makeItemDto("Item4 Name", "Item4 Description", true);

        itemService.create(userDto2.getId(), itemDto1);
        itemService.create(userDto2.getId(), itemDto2);
        itemService.create(userDto2.getId(), itemDto3);

        itemService.create(userDto3.getId(), itemDto4);

        List<ItemDto> items = List.of(itemDto1, itemDto2, itemDto3);

        for (ItemDto itemDto : items) {
            Item entity = ItemMapper.toItem(itemDto);
            em.persist(entity);
        }
        em.flush();

        List<ItemDto> receivedItems = itemService.findAll(userDto2.getId());

        assertThat(receivedItems, hasSize(items.size()));
        for (ItemDto itemDto : items) {
            assertThat(receivedItems, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(itemDto.getName())),
                    hasProperty("description", equalTo(itemDto.getDescription())),
                    hasProperty("available", equalTo(itemDto.getAvailable()))
            )));
        }

    }

    @Test
    void search() {
        UserDto userDto = makeUserDto("Иван Иванович", "ii@mail.ru");
        userService.create(userDto);

        ItemDto testItem1 = makeItemDto("Test Name", "Description", true);
        ItemDto testItem2 = makeItemDto(" Name", "test Description", true);
        ItemDto itemDto1 = makeItemDto("Item1 Name", "Item1 Description", true);

        itemService.create(1, testItem1);
        itemService.create(1, testItem2);
        itemService.create(1, itemDto1);
        List<ItemDto> items = List.of(testItem1, testItem2);

        List<ItemDto> receivedItems = itemService.search("test");
        log.info("receivedItems {}", receivedItems);
        log.info("items {}", items);

        assertThat(receivedItems, hasSize(items.size()));
        for (ItemDto itemDto : items) {
            assertThat(receivedItems, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(itemDto.getName())),
                    hasProperty("description", equalTo(itemDto.getDescription())),
                    hasProperty("available", equalTo(itemDto.getAvailable()))
            )));
        }


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
}