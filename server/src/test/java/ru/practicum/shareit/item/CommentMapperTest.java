package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    void setup() {
        // Arrange
        commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    @Test
    void toComment_ShouldMapCommentCreateDtoToComment() {
        // Arrange
        CommentCreateDto requestDto = new CommentCreateDto();
        requestDto.setText("The best of the best!");

        // Act
        Comment comment = commentMapper.toComment(requestDto);

        // Assert
        assertThat(comment).isNotNull();
        assertThat(comment.getText()).isEqualTo("The best of the best!");
    }

    @Test
    void toCommentDto_ShouldMapCommentToCommentDto() {
        // Arrange
        User author = new User();
        author.setName("Test name");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("The best of the best!");
        comment.setAuthor(author);

        // Act
        CommentDto commentDto = commentMapper.toCommentDto(comment);

        // Assert
        assertThat(commentDto).isNotNull();
        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("The best of the best!");
        assertThat(commentDto.getAuthorName()).isEqualTo("Test name");
    }

    @Test
    void getAuthorName_ShouldReturnUserName() {
        // Arrange
        User user = new User();
        user.setName("Test name");

        // Act
        String author = commentMapper.getAuthorName(user);

        // Assert
        assertThat(author).isEqualTo("Test name");
    }
}