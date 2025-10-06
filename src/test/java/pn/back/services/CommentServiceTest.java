package pn.back.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.repo.CommentRepository;
import pn.back.repo.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    /**
     * integrational test
     */
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
    // private MessageService messageService;
    @InjectMocks
    private CommentService commentService;

    @Mock
    private MessageRepository messageRepository;
    // private MessageService messageService;
    @InjectMocks
    private MessageService messageService;

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
    void getCommentsForPost() {
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(commentList);
        Optional<List<Comment>> result = commentService.getCommentsForPost(1L);
        assertTrue(result.isPresent());

    }

    @Test
    void getCommentByNumberForPost() {
        commentList = commentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(commentList);
        assertTrue(2 < commentRepository.getCommentsForMessage(1L).size());
        assertEquals(DEFAULT_CONTENT, commentRepository.getCommentsForMessage(1L).get(2).getContent());
    }


    @Test
    void addCommentsForPost() {
        when(messageRepository.findById(2L)).thenReturn(Optional.of(message));
        Optional<Message> optionalMessage = messageRepository.findById(2L);
        comment.setMessageKey(2L);
        comment.setId(7L);
        commentList.add(comment);
        Optional<Comment> res = commentService.addCommentsForPost(comment, 2L);
        Optional<Comment> res1 = Optional.of(comment);

        assertTrue(messageRepository.findById(2L).isPresent());
        assertTrue(res.isPresent());
        assertTrue(res1.isPresent());
    }

    @Test
    void editCommentsForPost() {
        commentList = commentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(commentList);
        assertTrue(2 < commentRepository.getCommentsForMessage(1L).size());
        System.out.println(commentList.size());
        Comment comment = commentList.get(1);
        comment.setContent(this.comment.getContent());
        Optional<Comment> result = Optional.of(comment);
        assertTrue(result.isPresent());
    }
}