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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));
        assertTrue(messageRepository.findById(1L).isPresent());
        assertEquals(messageRepository.findById(1L).get().getId(), testMessage.getId());
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
    void showAllByPageNotExists() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 2
                        && message.getContent().contains(ConfigTest.DEFAULT_CONTENT.substring(2, 8)))
                .toList();
        when(messageRepository.findAll()).thenReturn(testMessageList);
        assertNotEquals(3, messageRepository.findAll().size());
        assertEquals(0, messageRepository.findAll().size());
    }

    @Test
    void showAllByPage() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 2 && message.getContent()
                        .contains(ConfigTest.DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        System.out.println(testMessageList.size());
        when(messageRepository.findAll()).thenReturn(testMessageList);
        assertEquals(1, messageRepository.findAll().size());
        assertNotEquals(0, messageRepository.findAll().size());
    }

    @Test
    void updateContentTitle() {
        when(messageRepository.updateContentTitle(
                1L, ConfigTest.DEFAULT_TITLE + "-TTT",
                ConfigTest.DEFAULT_CONTENT + "-CCC")).thenReturn(Optional.of(testMessage));
        assertTrue(messageRepository.updateContentTitle(
                1L, ConfigTest.DEFAULT_TITLE + "-TTT", ConfigTest.DEFAULT_CONTENT + "-CCC").isPresent());

    }

    @Test
    void updateContent() {
        when(messageRepository.updateContent(1L,
                ConfigTest.DEFAULT_CONTENT + "-CCC")).thenReturn(Optional.of(testMessage));
        assertTrue(messageRepository.updateContent(1L, ConfigTest.DEFAULT_CONTENT + "-CCC").isPresent());
    }

    @Test
    void updateTitle() {
        when(messageRepository.updateTitle(1L, ConfigTest.DEFAULT_TITLE + "-TTT"))
                .thenReturn(Optional.of(testMessage));
        assertTrue(messageRepository.updateTitle(1L, ConfigTest.DEFAULT_TITLE + "-TTT").isPresent());
    }

    @Test
    void incrementCommentsCount() {
        when(messageRepository.incrementCommentsCount(testMessage)).thenReturn(Optional.of(testMessage));
        assertTrue(messageRepository.incrementCommentsCount(testMessage).isPresent());
    }

    @Test
    void incrementLikes() {
        when(messageRepository.incrementLikes(1L, 1L)).thenReturn(Optional.of(testMessage));
        assertTrue(messageRepository.incrementLikes(1, 1).isPresent());
    }

    @Test
    void save() {
        when(messageRepository.save(testMessage)).thenReturn(Optional.of(testMessage));
        assertTrue(messageRepository.save(testMessage).isPresent());
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
        assertEquals(6, messageRepository.commentsForMessage(testMessage).size());

    }
}