import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/testt"})
@SpringJUnitConfig({ShareItServer.class, UserService.class})
public class UserServiceTest {

    private final EntityManager em;
    private final UserService service;


    @Test
    void create() {
        UserDto userDto = makeUserDto("ii@mail.ru", "Иван Иванович");

        service.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();
        List<UserDto> users = service.findAll();
        log.info("Found {} users", users);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
    }


    @Test
    void findAll() {
        List<UserDto> users = List.of(
                makeUserDto("Иван Иванович", "ii@mail.ru"),
                makeUserDto("Петр Петрович", "pp@mail.ru")
        );
        for (UserDto user : users) {
            User entity = UserMapper.toUser(user);
            em.persist(entity);
        }
        em.flush();

        List<UserDto> receivedUsers = service.findAll();

        assertThat(receivedUsers, hasSize(users.size()));
        for (UserDto user : users) {
            assertThat(receivedUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(user.getName())),
                    hasProperty("email", equalTo(user.getEmail()))
            )));
        }
    }

    @Test
    void delete() {
        UserDto userDto = makeUserDto("Иван Иванович", "ii@mail.ru");
        service.create(userDto);

        service.delete(1);

        TypedQuery<User> query = em.createQuery("select u from User u where u.id = :id", User.class);
        try {
            query.setParameter("id", 1)
                    .getSingleResult();
        } catch (NoResultException thrown) {
            assertThat(thrown.getMessage(), equalTo("No result found for query [select u from User u where u.id = :id]"));
        }
    }


    private UserDto makeUserDto(String email, String name) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setName(name);

        return dto;
    }
}