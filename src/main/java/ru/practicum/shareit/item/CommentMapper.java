package ru.practicum.shareit.item;

import ru.practicum.shareit.user.UserMapper;

public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCreated(comment.getCreated());
        commentDto.setAuthorName(comment.getAuthorName());
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(ItemMapper.toItemDto(comment.getItem()));
        commentDto.setAuthor(UserMapper.toUserDto(comment.getAuthor()));
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(ItemMapper.toItem(commentDto.getItem()));
        comment.setAuthor(UserMapper.toUser(commentDto.getAuthor()));
        comment.setCreated(commentDto.getCreated());
        comment.setAuthorName(commentDto.getAuthorName());
        return comment;
    }
}
