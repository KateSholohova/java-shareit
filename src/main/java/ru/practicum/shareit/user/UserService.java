package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        userRepository.delete(id);
    }

    public UserDto update(int id, User newUser) {
        return UserMapper.toUserDto(userRepository.update(id, newUser));
    }

    public UserDto create(User user) {
        return UserMapper.toUserDto(userRepository.create(user));
    }

    public UserDto findById(int id) {
        return UserMapper.toUserDto(userRepository.findById(id));
    }

}
