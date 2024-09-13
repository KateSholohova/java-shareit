import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/test"})
@SpringJUnitConfig({ShareItServer.class, UserService.class, ItemService.class, ItemRequestService.class})
class ItemRequestServiceTest {


    private final EntityManager em;

    private final ItemRequestService itemRequestService;

    private final ItemService itemService;

    private final UserService userService;

    private ItemDto itemDto;
    private UserDto userDto;
    private UserDto userDto2;
    private ItemRequestDto requestDto;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Вещь 1");
        itemDto.setDescription("Описание вещи 1");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1);


        userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("John");
        userDto.setEmail("john@example.com");

        userDto2 = new UserDto();
        userDto2.setId(2);
        userDto2.setName("Петр Петрович");
        userDto2.setEmail("pp@mail.ru");

        requestDto = new ItemRequestDto();
        requestDto.setId(1);
        requestDto.setDescription("Описание запроса 1");
        requestDto.setCreated(LocalDateTime.now());

        userService.create(userDto);
        userService.create(userDto2);
    }


    @Test
    void getByUserId() {
        int userId = 2;
        itemRequestService.create(userId, requestDto);
        itemService.create(userDto.getId(), itemDto);

        List<ItemRequestDto> methodRequestList = itemRequestService.getByUserId(userId);
        log.info("{}", methodRequestList);

        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r where r.requester.id = :id", ItemRequest.class);
        List<ItemRequest> dbRequestList = query.setParameter("id", userId)
                .getResultList();

        assertEquals(methodRequestList.size(), (dbRequestList.size()));
        assertEquals(methodRequestList.get(0).getId(), (dbRequestList.get(0).getId()));
        assertEquals(methodRequestList.get(0).getDescription(), (dbRequestList.get(0).getDescription()));


        List<Integer> itemsRequestIds = List.of(itemDto.getRequestId());
        log.info("{}", itemsRequestIds);

        TypedQuery<Item> query1 = em.createQuery("select i from Item i where i.request.id in :id", Item.class);
        List<Item> dbItems = query1.setParameter("id", itemsRequestIds)
                .getResultList();
        log.info("{}", dbItems);

        assertEquals(1, (dbItems.get(0).getId()));
        assertEquals("Вещь 1", (dbItems.get(0).getName()));
    }

}