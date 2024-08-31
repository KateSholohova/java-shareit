package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBooker_Id(int userId);

    boolean existsByBooker_Id(int userId);

    List<Booking> findAllByItemIdIn(List<Integer> itemIds);

    List<Booking> findAllByItemId(int itemIds);

}
