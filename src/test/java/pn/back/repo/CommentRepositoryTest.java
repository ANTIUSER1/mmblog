package pn.back.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.CommentsTestConfig;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {CommentsTestConfig.class, MessageTestConfig.class})
class CommentRepositoryTest {

    @Autowired
    private List<Comment> testCommentList;
    @Autowired
    private Message testMessage;

    @Autowired
    private Comment testComment;


    @MockitoBean
    private CommentRepository commentRepository;


    @Test
    void findById() {
        when(commentRepository.findById(1L)).thenReturn(testCommentList.get(1));
        assertNotNull(commentRepository.findById(1L));
        assertNull(commentRepository.findById(100L));
    }

    @Test
    void getCommentsForMessage() {
        testCommentList = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.getCommentsForMessage(1L)).thenReturn(testCommentList);
        assertEquals(testCommentList, commentRepository.getCommentsForMessage(1L));
    }

    @Test
    void save() {
        List<Comment> commentList1 = testCommentList.stream().filter((comment) -> comment.getMessageKey() == 1).toList();
        when(commentRepository.save(testComment, testMessage)).thenReturn(Optional.of(testComment));
        int size = testCommentList.size();
        testCommentList.clear();
        testCommentList.add(testComment);
        testCommentList.addAll(commentList1);

        assertTrue(commentRepository.save(testComment, testMessage).isPresent());
        assertEquals(5, testCommentList.size());

    }

    @Test
    void update() {
        when(commentRepository.update(testComment)).thenReturn(Optional.of(testComment));
        assertTrue(commentRepository.update(testComment).isPresent());
    }
}