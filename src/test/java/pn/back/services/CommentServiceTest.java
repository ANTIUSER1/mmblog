package pn.back.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.CommentsTestConfig;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.repo.CommentRepository;
import pn.back.repo.MessageRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {CommentsTestConfig.class, MessageTestConfig.class})
class CommentServiceTest {

    @Autowired
    private Comment testComment;

    @Autowired
    private List<Comment> testCommentList;

    @MockitoBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService cmService;


    @Autowired
    private Message testMessage;

    @Mock
    private MessageRepository msgRepository;


    @Test
    void getCommentsForPost() {
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(
                testCommentList.stream().filter(c -> c.getId() == 1).toList());
        Optional<List<Comment>> result = cmService.getCommentsForPost(1L);
        System.out.println(result.isPresent());
        System.out.println(commentRepository.getCommentsForMessage(1L));
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get().size());

    }


    @Test
    void getCommentByNumberForPost() {
        testCommentList = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(testCommentList);
        Assertions.assertTrue(2 < commentRepository.getCommentsForMessage(1L).size());
    }


    @Test
    void addCommentsForPost() {
        when(msgRepository.findById(2L)).thenReturn(testMessage);
        Optional<Message> optionalMessage = Optional.ofNullable(msgRepository.findById(2L));
        testComment.setMessageKey(2L);
        testComment.setId(7L);
        testCommentList.add(testComment);
        when(commentRepository.save(testComment, testMessage)).thenReturn(Optional.of(testComment));
        Optional<Comment> res = cmService.addCommentsForPost(testComment, 2L);
        Optional<Comment> res1 = Optional.of(testComment);

        Assertions.assertNotNull(msgRepository.findById(2L));
//        Assertions.assertTrue(res.isPresent());
//        Assertions.assertTrue(res1.isPresent());
    }

    @Test
    void editCommentsForPost() {
        testCommentList = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(testCommentList);
        Assertions.assertTrue(2 < commentRepository.getCommentsForMessage(1L).size());
        Comment comment = testCommentList.get(1);
        comment.setContent(this.testComment.getContent());
        Optional<Comment> result = Optional.of(comment);
        Assertions.assertTrue(result.isPresent());
    }
}