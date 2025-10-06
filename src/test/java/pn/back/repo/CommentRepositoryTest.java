package pn.back.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentRepositoryTest {

    final String DEFAULT_TITLE = "Default title";
    final String DEFAULT_CONTENT_MESSAGE = "Default content MESSAGE";
    final String DEFAULT_CONTENT = "Default content ";
    final String DEFAULT_PICTURE_URI = "Default content";
    final long DEFAULT_LIKES_COUNT = 3;
    final long DEFAULT_COMMENTS_COUNT = 7;

    Message message;
    Comment comment;
    List<Comment> commentList = new ArrayList<>();
    Optional<List<Comment>> optionalComments;

    @Mock
    private CommentRepository commentRepository;


    @BeforeEach
    void init() {
        comment = new Comment(DEFAULT_CONTENT + "-TEST");
        message = new Message(2, DEFAULT_TITLE,
                DEFAULT_CONTENT_MESSAGE, DEFAULT_LIKES_COUNT, DEFAULT_COMMENTS_COUNT,
                null);

        commentList.add(new Comment(1L, DEFAULT_CONTENT, 1L));
        commentList.add(new Comment(2L, DEFAULT_CONTENT, 1L));
        commentList.add(new Comment(3L, DEFAULT_CONTENT, 1L));
        commentList.add(new Comment(4L, DEFAULT_CONTENT, 2L));
        commentList.add(new Comment(5L, DEFAULT_CONTENT, 2L));
        optionalComments = Optional.of(commentList);
    }


    @Test
    void findById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentList.get(1)));
        assertTrue(commentRepository.findById(1L).isPresent());
        assertFalse(commentRepository.findById(100L).isPresent());
    }

    @Test
    void getCommentsForMessage() {
        commentList = commentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(commentList);
        assertEquals(commentList, commentRepository.getCommentsForMessage(1L));
    }

    @Test
    void save() {
        List<Comment> commentList1 = commentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.save(comment, message)).thenReturn(Optional.of(comment));
        int size = commentList.size();
        commentList.clear();
        commentList.add(comment);
        commentList.addAll(commentList1);

        assertTrue(commentRepository.save(comment, message).isPresent());
        assertEquals(4, commentList.size());

    }

    @Test
    void update() {
        when(commentRepository.update(comment)).thenReturn(Optional.of(comment));
        assertTrue(commentRepository.update(comment).isPresent());
    }
}