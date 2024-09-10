package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> findAll(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findById(int userId, int itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> update(int userId, ItemDto item, int itemId) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> create(int userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> search(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("?text={text}", parameters);
    }

    public ResponseEntity<Object> createComment(int userId, int itemId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }


}
