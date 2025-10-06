package pn.back.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageRepositoryTest {


    final String DEFAULT_TITLE = "Default title";
    final String DEFAULT_CONTENT = "Default content";
    final String DEFAULT_PICTURE_URI = "Default content";
    final long DEFAULT_LIKES_COUNT = 3;
    final long DEFAULT_COMMENTS_COUNT = 7;

    List<Message> messageList = new ArrayList<>();
    Message message;

    List<Comment> commentList = new ArrayList<>();

    @Mock
    private MessageRepository messageRepository;


    @BeforeEach
    void init() {
        message = new Message(1, DEFAULT_TITLE,
                DEFAULT_CONTENT, DEFAULT_LIKES_COUNT, DEFAULT_COMMENTS_COUNT,
                null);

        messageList.add(new Message(1L, "title1",
                "content1", 10, 19,
                null));
        String[] tags = {"dd", "aa"};
        messageList.add(new Message(2L, "title2",
                "con  etnt 2", 100, 1,
                null, tags));

        messageList.add(new Message(3L, "title3",
                "content3", 8, 15,
                DEFAULT_PICTURE_URI));


        commentList.add(new Comment(1L, DEFAULT_CONTENT, 1L));
        commentList.add(new Comment(2L, DEFAULT_CONTENT, 1L));
    }


    @Test
    void findAll() {
        when(messageRepository.findAll()).thenReturn(messageList);
        assertEquals(3, messageRepository.findAll().size());
        assertEquals(messageList, messageRepository.findAll());

    }

    @Test
    void findById() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        assertTrue(messageRepository.findById(1L).isPresent());
        assertEquals(messageRepository.findById(1L).get().getId(), message.getId());
    }

    @Test
    void numberOfRecords() {
        messageList = messageList.stream()
                .filter(message -> message.getContent().contains(DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        when(messageRepository.numberOfRecords(DEFAULT_CONTENT)).thenReturn(1L);
        assertEquals(1L, messageRepository.numberOfRecords(DEFAULT_CONTENT));
    }

    @Test
    void showAllByPageNotExists() {
        messageList = messageList.stream()
                .filter(message -> message.getId() < 2
                        && message.getContent().contains(DEFAULT_CONTENT.substring(2, 8)))
                .toList();
        when(messageRepository.findAll()).thenReturn(messageList);
        assertNotEquals(3, messageRepository.findAll().size());
        assertEquals(0, messageRepository.findAll().size());
    }

    @Test
    void showAllByPage() {
        messageList = messageList.stream()
                .filter(message -> message.getId() < 2 && message.getContent()
                        .contains(DEFAULT_CONTENT.substring(8, 10)))
                .toList();
        System.out.println(messageList.size());
        when(messageRepository.findAll()).thenReturn(messageList);
        assertEquals(1, messageRepository.findAll().size());
        assertNotEquals(0, messageRepository.findAll().size());
    }

    @Test
    void updateContentTitle() {
        when(messageRepository.updateContentTitle(
                1L, DEFAULT_TITLE + "-TTT", DEFAULT_CONTENT + "-CCC")).thenReturn(Optional.of(message));
        assertTrue(messageRepository.updateContentTitle(
                1L, DEFAULT_TITLE + "-TTT", DEFAULT_CONTENT + "-CCC").isPresent());

    }

    @Test
    void updateContent() {
        when(messageRepository.updateContent(1L, DEFAULT_CONTENT + "-CCC")).thenReturn(Optional.of(message));
        assertTrue(messageRepository.updateContent(1L, DEFAULT_CONTENT + "-CCC").isPresent());
    }

    @Test
    void updateTitle() {
        when(messageRepository.updateTitle(1L, DEFAULT_TITLE + "-TTT")).thenReturn(Optional.of(message));
        assertTrue(messageRepository.updateTitle(1L, DEFAULT_TITLE + "-TTT").isPresent());
    }

    @Test
    void incrementCommentsCount() {
        when(messageRepository.incrementCommentsCount(message)).thenReturn(Optional.of(message));
        assertTrue(messageRepository.incrementCommentsCount(message).isPresent());
    }

    @Test
    void incrementLikes() {
        when(messageRepository.incrementLikes(1L, 1L)).thenReturn(Optional.of(message));
        assertTrue(messageRepository.incrementLikes(1, 1).isPresent());
    }

    @Test
    void save() {
        when(messageRepository.save(message)).thenReturn(Optional.of(message));
        assertTrue(messageRepository.save(message).isPresent());

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
        when(messageRepository.commentsForMessage(message)).thenReturn(commentList);
        assertEquals(2, messageRepository.commentsForMessage(message).size());

    }
}