package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ItemRepository {

    private final Map<Integer, Item> items = new HashMap<>();
    private int count = 0;
    private final UserRepository userRepository;

    public ItemDto create(int userId, Item item) {

        item.setOwner(userRepository.getOwner(userId));
        isParametersNotNull(item);
        item.setId(identify());
        items.put(item.getId(), item);
        log.info("Вещь создана: {}", item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto update(int userId, Item item, int itemId) {
        isOwner(userId, itemId);
        if (items.containsKey(itemId)) {

            Item oldItem = items.get(itemId);
            if (item.getName() != null) {
                oldItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                oldItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
            }

            log.info("Вещь обновлена: {}", oldItem);
            return ItemMapper.toItemDto(oldItem);
        }
        log.error("Нет вещи с данным id: {}", itemId);
        throw new NotFoundException("Вещь с id = " + itemId + " не найдена");

    }

    public ItemDto findById(int userId, int itemId) {
        return ItemMapper.toItemDto(items.get(itemId));
    }

    public List<ItemDto> findAll(int userId) {
        List<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (userId == item.getOwner().getId()) {
                userItems.add(item);
            }
        }
        return userItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String text) {
        List<Item> searchedItems = new ArrayList<>();
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                    item.getAvailable()) {
                searchedItems.add(item);
            }
        }

        return searchedItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    private void isParametersNotNull(Item item) {
        if (item.getName() == null || item.getName().isBlank()
                || item.getDescription() == null || item.getDescription().isBlank()
                || item.getAvailable() == null) {
            throw new ValidationException("Не заполнены все параметры");
        }
    }

    private void isOwner(int userId, int itemId) {
        if (items.get(itemId).getOwner().getId() != userId) {
            throw new NotFoundException("Неправильно указан id владельца");
        }
    }

    public int identify() {
        return ++count;
    }


}
