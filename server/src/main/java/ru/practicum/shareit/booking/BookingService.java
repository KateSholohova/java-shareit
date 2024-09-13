package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingDto create(int userId, BookingDto bookingDto) {
        if (itemRepository.existsById(bookingDto.getItemId()) && userRepository.existsById(userId)) {
            if (itemRepository.findById(bookingDto.getItemId()).get().getAvailable()) {
                bookingDto.setStatus(Status.WAITING);
                Booking booking = BookingMapper.toBooking(bookingDto);
                if (booking.getEnd().equals(booking.getStart())) {
                    throw new ValidationException("Начало бронирования не должно равняться концу");
                }
                booking.setBooker(userRepository.findById(userId).get());
                booking.setItem(itemRepository.findById(bookingDto.getItemId()).get());
                bookingRepository.save(booking);

                return BookingMapper.toBookingDto(booking);
            }
            throw new ValidationException("Вещь недоступна");
        }
        throw new NotFoundException("Не существует пользователя с id:  " + userId + " или вещи с id: " + bookingDto.getItemId());
    }

    public BookingDto approve(int userId, int bookingId, String approved) {
        if (userId == bookingRepository.findById(bookingId).get().getItem().getOwner().getId()) {
            Booking booking = bookingRepository.findById(bookingId).get();
            if (approved.equals("true")) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            bookingRepository.save(booking);
            return BookingMapper.toBookingDto(booking);
        }
        throw new ValidationException("Не правильный id пользователя " + userId);
    }

    public BookingDto getById(int bookingId, int userId) {
        if (userId == bookingRepository.findById(bookingId).get().getItem().getOwner().getId()
                || userId == bookingRepository.findById(bookingId).get().getBooker().getId()) {
            Booking booking = bookingRepository.findById(bookingId).get();
            return BookingMapper.toBookingDto(booking);
        }
        throw new ValidationException("Не правильный id пользователя " + userId);
    }

    public List<BookingDto> getByBookerId(int userId, State state) {
        List<Booking> bookings = bookingRepository.findAllByBooker_Id(userId);
        bookings.sort(Comparator.comparing(Booking::getStart).reversed());
        if (bookingRepository.existsByBooker_Id(userId)) {
            return checkState(userId, state, bookings);
        }

        throw new ValidationException("Не правильный id пользователя " + userId);
    }

    public List<BookingDto> getByOwnerId(int userId, State state) {
        if (itemRepository.existsByOwnerId(userId)) {
            List<Item> items = itemRepository.findAllByOwnerId(userId);
            List<Integer> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());
            List<Booking> bookings = bookingRepository.findAllByItemIdIn(itemIds);
            bookings.sort(Comparator.comparing(Booking::getStart).reversed());
            return checkState(userId, state, bookings);
        }

        throw new NotFoundException("Не правильный id пользователя " + userId);
    }


    public List<BookingDto> checkState(int userId, State state, List<Booking> bookings) {
        return switch (state) {
            case FUTURE -> bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case REJECTED -> bookings.stream()
                    .filter(booking -> booking.getStatus() == Status.REJECTED)
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case WAITING -> bookings.stream()
                    .filter(booking -> booking.getStatus() == Status.WAITING)
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case PAST -> bookings.stream()
                    .filter(booking -> booking.getStatus() == Status.CANCELED)
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case CURRENT -> bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now())
                            && booking.getEnd().isAfter(LocalDateTime.now()))
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            default -> bookings.stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        };
    }

}
