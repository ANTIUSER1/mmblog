package pn.back.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.back.config.ConfigTest;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.repo.CommentRepository;
import pn.back.repo.MessageRepository;

import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTest.class})
class CommentServiceTest {

    @Autowired
    private List<Comment> testCommentList;
    @Autowired
    private Comment testComment;
    @Autowired
    private Optional<List<Comment>> testOptionalComments;
    @Autowired
    private List<Message> testMessageList;
    @Autowired
    private Message testMessage;
    @Autowired
    private Message testMessageWithPicture;


    @Mock
    private MessageRepository messageRepository;
    @Mock
    private CommentRepository commentRepository;
    // private MessageService messageService;
    @InjectMocks
    private CommentService commentService;
    // private MessageService messageService;
    @InjectMocks
    private MessageService messageService;

//    @BeforeEach
//    void init() {
//        comment = new Comment(DEFAULT_CONTENT + "-TEST");
//        message = new Message(2, DEFAULT_TITLE,
//                DEFAULT_CONTENT_MESSAGE, DEFAULT_LIKES_COUNT, DEFAULT_COMMENTS_COUNT,
//                null);
//
//        commentList.add(new Comment(1L, DEFAULT_CONTENT, 1L));
//        commentList.add(new Comment(2L, DEFAULT_CONTENT, 1L));
//        commentList.add(new Comment(3L, DEFAULT_CONTENT, 1L));
//        commentList.add(new Comment(4L, DEFAULT_CONTENT, 2L));
//        commentList.add(new Comment(5L, DEFAULT_CONTENT, 2L));
//        optionalComments = Optional.of(commentList);
//    }

    @Test
    void getCommentsForPost() {
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(
                testCommentList.stream().filter(c -> c.getId() == 1).toList());
        Optional<List<Comment>> result = commentService.getCommentsForPost(1L);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(1, result.get().get(0).getId());

    }

    @Test
    void getCommentByNumberForPost() {
        testCommentList = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(testCommentList);
        assertTrue(2 < commentRepository.getCommentsForMessage(1L).size());
        assertEquals(ConfigTest.DEFAULT_CONTENT, commentRepository.getCommentsForMessage(1L).get(2).getContent());
    }


    @Test
    void addCommentsForPost() {
        when(messageRepository.findById(2L)).thenReturn(testMessage);
        Optional<Message> optionalMessage = Optional.ofNullable(messageRepository.findById(2L));
        testComment.setMessageKey(2L);
        testComment.setId(7L);
        testCommentList.add(testComment);
        when(commentRepository.save(testComment, testMessage)).thenReturn(Optional.of(testComment));
        Optional<Comment> res = commentService.addCommentsForPost(testComment, 2L);
        Optional<Comment> res1 = Optional.of(testComment);

        assertNotNull(messageRepository.findById(2L));
        assertTrue(res.isPresent());
        assertTrue(res1.isPresent());
    }

    @Test
    void editCommentsForPost() {
        testCommentList = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(testCommentList);
        assertTrue(2 < commentRepository.getCommentsForMessage(1L).size());
        Comment comment = testCommentList.get(1);
        comment.setContent(this.testComment.getContent());
        Optional<Comment> result = Optional.of(comment);
        assertTrue(result.isPresent());
    }
}