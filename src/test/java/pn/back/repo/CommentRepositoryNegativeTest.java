package pn.back.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.CommentsTestConfig;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@Import({CommentsTestConfig.class, MessageTestConfig.class})
class CommentRepositoryNegativeTest {

    @Autowired
    private List<Comment> testCommentList;
    @Autowired
    private List<Message> testMessageList;
    @Autowired
    private Message testMessage;
    @Autowired
    private Message testMessageWithPicture;
    @Autowired
    private Comment testComment;

    @MockitoBean
    private CommentRepository commentRepository;


    @Test
    void findById() {
        when(commentRepository.findById(1L)).thenReturn(testComment);
        assertNotEquals(3, commentRepository.findById(1L));
    }

    @Test
    void getCommentsForMessage() {
        testCommentList = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        List<Comment> cl = testCommentList.stream().filter(c -> c.getMessageKey() > 2).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(testCommentList);
        assertNotEquals(cl, commentRepository.getCommentsForMessage(1L));
    }

    @Test
    void save() {
        List<Comment> commentList1 = testCommentList.stream()
                .filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.save(testComment, testMessage)).thenReturn(Optional.of(testComment));
        int size = testCommentList.size();
        testCommentList.clear();
        testCommentList.add(testComment);
        testCommentList.addAll(commentList1);

        assertFalse(commentRepository.save(testComment, testMessage).isEmpty());
        assertNotEquals(10, testCommentList.size());

    }

    @Test
    void update() {
        when(commentRepository.update(testComment)).thenReturn(Optional.of(testComment));
        assertFalse(commentRepository.update(testComment).isEmpty());
    }
}