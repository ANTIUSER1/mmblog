package pn.back.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pn.back.entities.Message;
import pn.back.repo.MessageRepository;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    final String DEFAULT_TITLE = "Default title";
    final String DEFAULT_CONTENT = "Default content";
    final String DEFAULT_PICTURE_URI = "Default content";
    final long DEFAULT_LIKES_COUNT = 3;
    final long DEFAULT_COMMENTS_COUNT = 7;

    List<Message> messageList = new ArrayList<>();
    Message message;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void init() {
        message = new Message(0, DEFAULT_TITLE,
                DEFAULT_CONTENT, DEFAULT_LIKES_COUNT, DEFAULT_COMMENTS_COUNT,
                null);

        messageList.add(new Message(1L, "title1",
                "content1", 10, 19,
                null));
        String[] tags = {"dd", "aa"};
        messageList.add(new Message(2L, "title2",
                "content2", 100, 1,
                null, tags));

        messageList.add(new Message(3L, "title3",
                "content3", 8, 15,
                DEFAULT_PICTURE_URI));
    }

    @Test
    void findById() {
        when(messageService.findAll()).thenReturn(messageList);

        assertEquals(3, messageService.findAll().size());
    }

    @Test
    void showAllPG() {
        long total = messageList.size();
        int limit = 2;
        int page = 0;
        int last = (int) (total / limit);
        assertTrue(page < last);
        page = 1;
        last = (int) (total / limit);
        assertFalse(page < last);
    }

    @Test
    void modifyMessage() {
        Message m = messageList.get(0);
        m.setTitle(DEFAULT_TITLE);
        m.setContent(DEFAULT_CONTENT);
        m.setLikesCount(DEFAULT_LIKES_COUNT);
        assertTrue(m.getContent().equals(DEFAULT_CONTENT) && m.getTitle().equals(DEFAULT_TITLE));
        assertTrue(m.getCommentsCount() != DEFAULT_COMMENTS_COUNT);
    }

    @Test
    void incrementLikes() {
        Message m = messageList.get(0);
        long oldLikesCount = m.getLikesCount();
        m.setLikesCount(oldLikesCount + 1);
        assertEquals(1, m.getLikesCount() - oldLikesCount);
    }

    @Test
    void addMessage() {
        int oldSize = messageList.size();
        messageList.add(message);
        assertEquals(1, messageList.size() - oldSize);
    }

    @Test
    void delete() {
        int oldSize = messageList.size();
        messageList.remove(messageList.get(1));
        assertEquals(-1, messageList.size() - oldSize);
    }

    @Test
    void getPicture() {
        Message m = messageList.get(0);
        assertNull(m.getPictureUrl());
        m = messageList.get(2);
        assertNotNull(m.getPictureUrl());
        assertEquals(DEFAULT_PICTURE_URI, m.getPictureUrl());
    }

}