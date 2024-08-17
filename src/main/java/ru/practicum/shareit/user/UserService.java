package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        return userRepository.findAll();
    }

    public void delete(int id) {
        userRepository.delete(id);
    }

    public UserDto update(int id, User newUser) {
        return userRepository.update(id, newUser);
    }

    public UserDto create(User user) {
        return userRepository.create(user);
    }

    public UserDto findById(int id) {
        return userRepository.findById(id);
    }

}
