package pn.back.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@Import({CommentsTestConfig.class, MessageTestConfig.class})
class CommentServiceNegativeTest {

    @Autowired
    private Comment testComment;

    @Autowired
    private List<Comment> testCommentList;

    @Mock
    private CommentRepository commentRepository;

    @Autowired
    private CommentService cmService;


    @Autowired
    private Message testMessage;

    @Mock
    private MessageRepository msgRepository;


    @Test
    void getCommentsForPost() {
        when(commentRepository.getCommentsForMessage(11L)).thenReturn(
                testCommentList.stream().filter(c -> c.getId() == 7).toList());
        Optional<List<Comment>> result = cmService.getCommentsForPost(1L);
        System.out.println(result.isPresent());
        System.out.println(commentRepository.getCommentsForMessage(1L));
        Assertions.assertNotEquals(3, result.get().size());

    }


    @Test
    void getCommentByNumberForPost() {
        testCommentList = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 111).toList();
        when(commentRepository.getCommentsForMessage(111L)).thenReturn(testCommentList);
        Assertions.assertFalse(2 < commentRepository.getCommentsForMessage(1L).size());
        //  assertEquals(ConfigTest.DEFAULT_CONTENT, commentRepository.getCommentsForMessage(1L).get(2).getContent());
    }


    @Test
    void addCommentsForPost() {
        when(msgRepository.findById(211L)).thenReturn(testMessage);
        Optional<Message> optionalMessage = Optional.ofNullable(msgRepository.findById(211L));
        when(commentRepository.save(testComment, testMessage)).thenReturn(Optional.of(testComment));
        Optional<Comment> res = cmService.addCommentsForPost(testComment, 211L);
        Optional<Comment> res1 = Optional.of(testComment);
        System.out.println(res1.get());
        Assertions.assertNotEquals(211L, msgRepository.findById(211L));
    }

    @Test
    void editCommentsForPost() {
        testCommentList = testCommentList
                .stream()
                .filter((comment) -> comment.getMessageKey() == 111)
                .toList();
        when(commentRepository.getCommentsForMessage(111L)).thenReturn(testCommentList);
        Assertions.assertFalse(
                2 < commentRepository.getCommentsForMessage(111L).size());
        System.out.println(commentRepository.getCommentsForMessage(111L).size());
    }
}