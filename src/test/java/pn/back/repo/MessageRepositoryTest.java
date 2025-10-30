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

import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTest.class})
class MessageRepositoryTest {


    @Autowired
    List<Comment> testCommentList;
    @Autowired
    private List<Message> testMessageList;
    @Autowired
    private Message testMessage;
    @Autowired
    private Message testMessageWithPicture;

    @Mock
    private MessageRepository messageRepository;


    @Test
    void findAll() {
        when(messageRepository.findAll()).thenReturn(testMessageList);
        assertEquals(3, messageRepository.findAll().size());
        assertEquals(testMessageList, messageRepository.findAll());

    }

    @Test
    void findById() {
        when(messageRepository.findById(1L)).thenReturn(testMessage);
        assertEquals(1000, messageRepository.findById(1L).getId());
    }

    @Test
    void numberOfRecords() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getContent().contains(ConfigTest.DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        when(messageRepository.numberOfRecords(ConfigTest.DEFAULT_CONTENT)).thenReturn(1L);
        assertEquals(1L, messageRepository.numberOfRecords(ConfigTest.DEFAULT_CONTENT));
    }


    @Test
    void showAllByPage() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 2 && message.getContent()
                        .contains(ConfigTest.DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        when(messageRepository.findAll()).thenReturn(testMessageList);
        assertEquals(1, messageRepository.findAll().size());

    }

    @Test
    void updateContentTitle() {
        when(messageRepository.updateContentTitle(
                1L, ConfigTest.DEFAULT_TITLE + "-TTT",
                ConfigTest.DEFAULT_CONTENT + "-CCC")).thenReturn(testMessage);
        assertEquals("Default title",
                messageRepository.updateContentTitle(
                        1L, ConfigTest.DEFAULT_TITLE + "-TTT",
                        ConfigTest.DEFAULT_CONTENT + "-CCC").getTitle());

    }

    @Test
    void updateContent() throws SQLException {
        when(messageRepository.updateContent(1L,
                ConfigTest.DEFAULT_CONTENT + "-CCC")).thenReturn(testMessage);
        assertEquals("Default content",
                messageRepository.updateContent(
                        1L, ConfigTest.DEFAULT_CONTENT + "-CCC").getContent());
    }

    @Test
    void updateTitle() {
        when(messageRepository.updateTitle(1L, ConfigTest.DEFAULT_TITLE + "-TTT"))
                .thenReturn(testMessage);
        assertEquals("Default title",
                messageRepository.updateTitle(1L, ConfigTest.DEFAULT_TITLE + "-TTT").getTitle()
        );
    }

    @Test
    void incrementCommentsCount() {
        testMessage.setCommentsCount(3);
        when(messageRepository.incrementCommentsCount(testMessage)).thenReturn(testMessage);
        assertEquals(3,
                messageRepository.incrementCommentsCount(testMessage).getCommentsCount());
    }

    @Test
    void incrementLikes() {
        when(messageRepository.incrementLikes(1L, 1L)).thenReturn(testMessage);
        assertNotNull(messageRepository.incrementLikes(1, 1));
    }

    @Test
    void save() {
        when(messageRepository.save(testMessage)).thenReturn(testMessage);
        assertEquals(testMessage, messageRepository.save(testMessage));
    }

    @Test
    void delete() {
        when(messageRepository.delete(1)).thenReturn(1L);
        assertEquals(1, messageRepository.delete(1L));
    }

    @Test
    void addPicture() {
        when(messageRepository.addPicture(1L, "111")).thenReturn(true);
        assertTrue(messageRepository.addPicture(1L, "111"));
    }

    @Test
    void commentsForMessage() {
        when(messageRepository.commentsForMessage(testMessage)).thenReturn(testCommentList);
        assertEquals(4, messageRepository.commentsForMessage(testMessage).size());

    }
}