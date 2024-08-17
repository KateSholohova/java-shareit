package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemDto> findAll(int userId) {
        return itemRepository.findAll(userId);
    }

    public ItemDto findById(int userId, int itemId) {
        return itemRepository.findById(userId, itemId);
    }

    public ItemDto update(int userId, Item item, int itemId) {
        return itemRepository.update(userId, item, itemId);
    }

    public ItemDto create(int userId, Item item) {
        return itemRepository.create(userId, item);
    }

    public List<ItemDto> search(String text) {
        return itemRepository.search(text);
    }


}
