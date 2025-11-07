package pn.back.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.CommentsTestConfig;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration( classes ={  MessageTestConfig.class})
class MessageRepositoryTest {


    @Autowired
    private List<Comment> testCommentList;
    @Autowired
    private List<Message> testMessageList;
    @Autowired
    private Message testMessage;


    @MockitoBean
    private MessageRepository messageRepository;


    @Test
    void findAll() {
        when(messageRepository.findAll()).thenReturn(testMessageList);
        Assertions.assertEquals(3, messageRepository.findAll().size());
        Assertions.assertEquals(testMessageList, messageRepository.findAll());

    }


    @Test
    void findById() {
        when(messageRepository.findById(1L)).thenReturn(testMessage);
        Assertions.assertEquals(1000, messageRepository.findById(1L).getId());
    }

    @Test
    void numberOfRecords() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getContent().contains(MessageTestConfig.DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        when(messageRepository.numberOfRecords(MessageTestConfig.DEFAULT_CONTENT)).thenReturn(1L);
        Assertions.assertEquals(1L, messageRepository.numberOfRecords(MessageTestConfig.DEFAULT_CONTENT));
    }


    @Test
    void showAllByPage() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 2 && message.getContent()
                        .contains(MessageTestConfig.DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        when(messageRepository.findAll()).thenReturn(testMessageList);
        Assertions.assertEquals(1, messageRepository.findAll().size());

    }

    @Test
    void updateContentTitle() {
        when(messageRepository.updateContentTitle(
                1L, MessageTestConfig.DEFAULT_TITLE + "-TTT",
                MessageTestConfig.DEFAULT_CONTENT + "-CCC")).thenReturn(testMessage);
        Assertions.assertEquals("Default title",
                messageRepository.updateContentTitle(
                        1L, MessageTestConfig.DEFAULT_TITLE + "-TTT",
                        MessageTestConfig.DEFAULT_CONTENT + "-CCC").getTitle());

    }

    @Test
    void updateContent() throws SQLException {
        when(messageRepository.updateContent(1L,
                MessageTestConfig.DEFAULT_CONTENT + "-CCC")).thenReturn(testMessage);
        Assertions.assertEquals("Default content",
                messageRepository.updateContent(
                        1L, MessageTestConfig.DEFAULT_CONTENT + "-CCC").getContent());
    }

    @Test
    void updateTitle() {
        when(messageRepository.updateTitle(1L, MessageTestConfig.DEFAULT_TITLE + "-TTT"))
                .thenReturn(testMessage);
        Assertions.assertEquals("Default title",
                messageRepository.updateTitle(1L, MessageTestConfig.DEFAULT_TITLE + "-TTT")
                        .getTitle()
        );
    }

    @Test
    void incrementCommentsCount() {
        testMessage.setCommentsCount(3);
        when(messageRepository.incrementCommentsCount(testMessage)).thenReturn(testMessage);
        Assertions.assertEquals(3,
                messageRepository.incrementCommentsCount(testMessage).getCommentsCount());
    }

    @Test
    void incrementLikes() {
        when(messageRepository.incrementLikes(1L, 1L)).thenReturn(testMessage);
        Assertions.assertNotNull(messageRepository.incrementLikes(1, 1));
    }

    @Test
    void save() {
        when(messageRepository.save(testMessage)).thenReturn(testMessage);
        Assertions.assertEquals(testMessage, messageRepository.save(testMessage));
    }

    @Test
    void delete() {
        when(messageRepository.delete(1)).thenReturn(1L);
        Assertions.assertEquals(1, messageRepository.delete(1L));
    }

    @Test
    void addPicture() {
        when(messageRepository.addPicture(1L, "111")).thenReturn(true);
        Assertions.assertTrue(messageRepository.addPicture(1L, "111"));
    }

    @Test
    void commentsForMessage() {
        when(messageRepository.commentsForMessage(testMessage)).thenReturn(testCommentList);
        Assertions.assertEquals(9, messageRepository.commentsForMessage(testMessage).size());

    }
}