package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.SameEmailException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public UserDto update(int id, UserDto newUserDto) {
        User newUser = UserMapper.toUser(newUserDto);
        newUser.setId(id);
        if (userRepository.existsById(id)) {
            if (isSameEmail(newUser)) {
                throw new SameEmailException(newUser.getEmail());
            }
            User oldUser = userRepository.findById(id).get();
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            userRepository.save(oldUser);
            log.info("Пользователь обновлен: {}", oldUser);
            return UserMapper.toUserDto(oldUser);
        }
        log.error("Нет пользователя с данным id: {}", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (isSameEmail(user)) {
            throw new SameEmailException(user.getEmail());
        }
        if (user.getEmail() == null) {
            throw new ValidationException("Email должен быть указан");
        }
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public UserDto findById(int id) {
        if (userRepository.existsById(id)) {
            return UserMapper.toUserDto(userRepository.findById(id).get());
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");

    }

    private boolean isSameEmail(User user) {
        if (!userRepository.findAll().isEmpty()) {
            for (User u : userRepository.findAll()) {
                if (u.getEmail().equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

}
