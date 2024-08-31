package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public List<ItemDto> findAll(int userId) {
        List<ItemDto> itemDtoList = itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        for (ItemDto itemDto : itemDtoList) {
            List<Booking> bookings = bookingRepository.findAllByItemId(itemDto.getId());
            if (!bookings.isEmpty()) {
                log.info("Bookings: {}", bookings);
                List<LocalDateTime> lastBooking = new ArrayList<>();
                List<LocalDateTime> nextBooking = new ArrayList<>();
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now()) || booking.getEnd().isEqual(LocalDateTime.now())) {
                        lastBooking.add(booking.getEnd());
                    } else {
                        nextBooking.add(booking.getStart());
                    }

                }
                Collections.sort(nextBooking);
                Collections.sort(lastBooking);
                log.info("Next booking: {}", nextBooking);
                log.info("Last booking: {}", lastBooking);
                if (!nextBooking.isEmpty()) {
                    itemDto.setNextBooking(nextBooking.getFirst());
                }
                if (!lastBooking.isEmpty()) {
                    itemDto.setLastBooking(lastBooking.getLast());
                }
            }

            log.info("ItemDto: {}", itemDto);
            List<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());
            if (comments == null || comments.isEmpty()) {
                comments = new ArrayList<>();
            }
            itemDto.setComments(comments);
        }

        return itemDtoList;
    }

    public ItemDto findById(int userId, int itemId) {
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(itemId).get());
        log.info("ItemDto: {}", itemDto);
        List<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());
        if (comments == null || comments.isEmpty()) {
            comments = new ArrayList<>();
        }
        itemDto.setComments(comments);
        return itemDto;
    }


    public ItemDto update(int userId, ItemDto item, int itemId) {
        if (userRepository.existsById(userId)) {
            isOwner(userId, itemId);
            if (itemRepository.existsById(itemId)) {

                Item oldItem = itemRepository.findById(itemId).get();
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

        throw new NotFoundException("Пользователь с id = " + userId + " не найден");

    }

    public ItemDto create(int userId, ItemDto itemDto) {
        if (userRepository.existsById(userId)) {
            Item item = ItemMapper.toItem(itemDto);
            item.setOwner(userRepository.findById(userId).get());
            isParametersNotNull(item);
            itemRepository.save(item);
            boolean exists = itemRepository.existsById(item.getId());
            log.info("Вещь создана: {}   {}", item, exists);
            return ItemMapper.toItemDto(item);
        }
        throw new NotFoundException("Пользователь с id = " + userId + " не найден");
    }

    public List<ItemDto> search(String text) {
        List<Item> searchedItems = new ArrayList<>();
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        for (Item item : itemRepository.findAll()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                    item.getAvailable()) {
                searchedItems.add(item);
            }
        }
        List<ItemDto> searchedItemsDto = searchedItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        return searchedItemsDto;
    }

    public Comment createComment(int userId, int itemId, Comment comment) {

        for (Booking booking : bookingRepository.findAllByBooker_Id(userId)) {
            if (booking.getItem().getId() == itemId && booking.getEnd().isBefore(LocalDateTime.now())) {
                comment.setItem(itemRepository.findById(itemId).get());
                comment.setAuthor(userRepository.findById(userId).get());
                comment.setAuthorName(comment.getAuthor().getName());
                comment.setCreated(LocalDateTime.now());
                commentRepository.save(comment);
                return comment;
            }
        }
        throw new ValidationException("Неправильные id или время окончания бронирования ещё не наступило");
    }


    private void isParametersNotNull(Item item) {
        if (item.getName() == null || item.getName().isBlank()
                || item.getDescription() == null || item.getDescription().isBlank()
                || item.getAvailable() == null) {
            throw new ValidationException("Не заполнены все параметры");
        }
    }

    private void isOwner(int userId, int itemId) {
        if (itemRepository.findById(itemId).get().getOwner().getId() != userId) {
            throw new NotFoundException("Неправильно указан id владельца");
        }
    }


}
