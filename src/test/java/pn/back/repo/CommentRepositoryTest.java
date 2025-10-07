package pn.back.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.back.config.ConfigTest;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTest.class})
class CommentRepositoryTest {

    @Autowired
    List<Comment> testCommentList;
    @Autowired
    private List<Message> testMessageList;
    @Autowired
    private Message testMessage;
    @Autowired
    private Message testMessageWithPicture;
    @Autowired
    private Comment testComment;

    @Mock
    private CommentRepository commentRepository;

    
    @Test
    void findById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testCommentList.get(1)));
        assertTrue(commentRepository.findById(1L).isPresent());
        assertFalse(commentRepository.findById(100L).isPresent());
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
        assertEquals(4, testCommentList.size());

    }

    @Test
    void update() {
        when(commentRepository.update(testComment)).thenReturn(Optional.of(testComment));
        assertTrue(commentRepository.update(testComment).isPresent());
    }
}