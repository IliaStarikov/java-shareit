package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = "spring", nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CommentMapper {

    Comment toComment(CommentCreateDto requestDto);

    @Mapping(target = "authorName", expression = "java(getAuthorName(comment.getAuthor()))")
    CommentDto toCommentDto(Comment comment);

    default String getAuthorName(User user) {
        return user.getName();
    }
}