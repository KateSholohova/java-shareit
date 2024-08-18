package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemDto> findAll(int userId) {
        return itemRepository.findAll(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto findById(int userId, int itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(userId, itemId));
    }

    public ItemDto update(int userId, ItemDto item, int itemId) {
        return ItemMapper.toItemDto(itemRepository.update(userId, ItemMapper.toItem(item), itemId));

    }

    public ItemDto create(int userId, ItemDto item) {
        return ItemMapper.toItemDto(itemRepository.create(userId, ItemMapper.toItem(item)));
    }

    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


}
