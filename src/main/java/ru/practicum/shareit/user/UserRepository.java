package ru.practicum.shareit.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.SameEmailException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRepository {

    private final Map<Integer, User> users = new HashMap<>();
    private int count = 0;

    public UserDto create(User user) {
        if (isSameEmail(user)) {
            throw new SameEmailException(user.getEmail());
        }
        if (user.getEmail() == null) {
            throw new ValidationException("Email должен быть указан");
        }
        user.setId(identify());
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return UserMapper.toUserDto(user);
    }

    public UserDto update(int id, User newUser) {
        newUser.setId(id);

        if (users.containsKey(newUser.getId())) {
            if (isSameEmail(newUser)) {
                throw new SameEmailException(newUser.getEmail());
            }
            User oldUser = users.get(newUser.getId());
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            log.info("Пользователь обновлен: {}", oldUser);
            return UserMapper.toUserDto(oldUser);
        }
        log.error("Нет пользователя с данным id: {}", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }


    public void delete(int id) {
        if (users.containsKey(id)) {
            users.remove(id);
        }
    }

    public List<UserDto> findAll() {
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(int id) {
        return UserMapper.toUserDto(users.get(id));
    }

    public int identify() {
        return ++count;
    }

    private boolean isSameEmail(User user) {
        if (!users.isEmpty()) {
            for (User u : users.values()) {
                if (u.getEmail().equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    public User getOwner(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }


}
