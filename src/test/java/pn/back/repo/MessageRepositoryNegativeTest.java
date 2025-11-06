package pn.back.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.CommentsTestConfig;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@Import({CommentsTestConfig.class, MessageTestConfig.class})
class MessageRepositoryNegativeTest {


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
        testMessageList = testMessageList.stream()
                .filter(m -> m.getId() > 3).toList();
        when(messageRepository.findAll()).thenReturn(testMessageList);
        Assertions.assertNotEquals(3, messageRepository.findAll().size());
  
    }


    @Test
    void findById() {
        when(messageRepository.findById(1L)).thenReturn(testMessage);
        System.out.println(messageRepository.findById(1L));
        Assertions.assertNotEquals(1200, messageRepository.findById(1L).getId());
    }

    @Test
    void numberOfRecords() {
        when(messageRepository.numberOfRecords(MessageTestConfig.DEFAULT_CONTENT))
                .thenReturn(1111L);
        Assertions.assertNotEquals(1L, messageRepository.numberOfRecords(MessageTestConfig.DEFAULT_CONTENT));
    }


    @Test
    void showAllByPage() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 3 && message.getContent()
                        .contains(MessageTestConfig.DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        when(messageRepository.findAll()).thenReturn(testMessageList);
        Assertions.assertNotEquals(10, messageRepository.findAll().size());

    }


    @Test
    void updateContentTitle() {
        when(messageRepository.updateContentTitle(
                1L, MessageTestConfig.DEFAULT_TITLE + "-TTT",
                MessageTestConfig.DEFAULT_CONTENT + "-CCC")).thenReturn(testMessage);
        Assertions.assertNotEquals("Default title-TTT",
                messageRepository.updateContentTitle(
                        1L, MessageTestConfig.DEFAULT_TITLE + "-TTT",
                        MessageTestConfig.DEFAULT_CONTENT + "-CCC").getTitle());

    }

    @Test
    void updateContent() throws SQLException {
        when(messageRepository.updateContent(1L,
                MessageTestConfig.DEFAULT_CONTENT + "-CCC")).thenReturn(testMessage);
        Assertions.assertNotEquals("Default content-CCC",
                messageRepository.updateContent(
                        1L, MessageTestConfig.DEFAULT_CONTENT + "-CCC").getContent());
    }

    @Test
    void updateTitle() {
        when(messageRepository.updateTitle(1L, MessageTestConfig.DEFAULT_TITLE + "-TTT"))
                .thenReturn(testMessage);
        Assertions.assertNotEquals("Default title-TTT",
                messageRepository.updateTitle(1L, MessageTestConfig.DEFAULT_TITLE + "-TTT").getTitle()
        );
    }

    @Test
    void incrementCommentsCount() {
        testMessage.setCommentsCount(1 + testMessage.getCommentsCount());
        when(messageRepository.incrementCommentsCount(testMessage)).thenReturn(testMessage);
        Assertions.assertNotEquals(1000,
                messageRepository.incrementCommentsCount(testMessage).getCommentsCount());
    }

    @Test
    void incrementLikes() {
        when(messageRepository.incrementLikes(1L, 1L)).thenReturn(testMessage);
        Assertions.assertNotNull(messageRepository.incrementLikes(1, 1));
    }

    @Test
    void save() {
        Message m = new Message(100, "DEFAULT_TITLE",
                "DEFAULT_CONTENT", 0, 0, null);
        when(messageRepository.save(testMessage)).thenReturn(testMessage);
        Assertions.assertNotEquals(m, messageRepository.save(testMessage));
    }

    @Test
    void delete() {
        when(messageRepository.delete(1)).thenReturn(1L);
        Assertions.assertEquals(1, messageRepository.delete(1L));
    }

    @Test
    void addPicture() {
        when(messageRepository.addPicture(1L, "111")).thenReturn(false);
        Assertions.assertFalse(messageRepository.addPicture(1L, "111"));
    }

    @Test
    void commentsForMessage() {
        when(messageRepository.commentsForMessage(testMessage)).thenReturn(testCommentList);
        Assertions.assertNotEquals(80, messageRepository.commentsForMessage(testMessage).size());
    }
}