package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getByUsesId(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestClient.getByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return itemRequestClient.getAll();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable("requestId") int requestId) {
        return itemRequestClient.findById(requestId);
    }
}
