package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemFotRequest;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestDto create(Integer userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(userRepository.findById(userId).get());
        itemRequestRepository.save(itemRequest);
        log.info("Created new item request: {}", itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    public List<ItemRequestDto> getByUserId(Integer userId) {
        List<ItemRequestDto> itemRequestDto = itemRequestRepository.findAllByRequesterId(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        for (ItemRequestDto requestDto : itemRequestDto) {
            List<ItemFotRequest> itemFotRequestList = itemRepository.findAllByRequestId(userId).stream()
                    .map(ItemMapper::toItemFotRequest)
                    .collect(Collectors.toList());
            requestDto.setItems(itemFotRequestList);
        }
        itemRequestDto.sort(Comparator.comparing(ItemRequestDto::getCreated).reversed());
        return itemRequestDto;
    }

    public List<ItemRequestDto> getAll() {
        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository.findAll().stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        itemRequestDtoList.sort(Comparator.comparing(ItemRequestDto::getCreated).reversed());
        return itemRequestDtoList;
    }

    public ItemRequestDto findById(Integer id) {
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequestRepository.findById(id).get());
        List<ItemFotRequest> itemFotRequestList = itemRepository.findAllByRequestId(id).stream()
                .map(ItemMapper::toItemFotRequest)
                .collect(Collectors.toList());
        itemRequestDto.setItems(itemFotRequestList);
        return itemRequestDto;

    }


}
