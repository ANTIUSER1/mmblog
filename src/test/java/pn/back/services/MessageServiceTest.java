package pn.back.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.back.config.ConfigTests;
import pn.back.entities.Message;
import pn.back.repo.MessageRepository;

import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTests.class})
class MessageServiceTest {


    @Autowired
    private List<Message> testMessageList;
    @Autowired
    private Message testMessage;
    @Autowired
    private Message testMessageWithPicture;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessageService messageService;


    @Test
    void findById() {
        when(messageService.findAll()).thenReturn(testMessageList);

        assertEquals(3, messageService.findAll().size());
    }

    @Test
    void showAllPG() {
        long total = testMessageList.size();
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
        Message m = testMessageList.get(0);
        m.setTitle(ConfigTests.DEFAULT_TITLE);
        m.setContent(ConfigTests.DEFAULT_CONTENT);
        m.setLikesCount(ConfigTests.DEFAULT_LIKES_COUNT);
        assertTrue(m.getContent().equals(ConfigTests.DEFAULT_CONTENT) && m.getTitle().equals(ConfigTests.DEFAULT_TITLE));
        assertTrue(m.getCommentsCount() != ConfigTests.DEFAULT_COMMENTS_COUNT);
    }

    @Test
    void incrementLikes() {
        Message m = testMessageList.get(0);
        long oldLikesCount = m.getLikesCount();
        m.setLikesCount(oldLikesCount + 1);
        assertEquals(1, m.getLikesCount() - oldLikesCount);
    }

    @Test
    void addMessage() {
        int oldSize = testMessageList.size();
        testMessageList.add(testMessage);
        assertEquals(1, testMessageList.size() - oldSize);
    }

    @Test
    void delete() {
        int oldSize = testMessageList.size();
        testMessageList.remove(testMessageList.get(1));
        assertEquals(-1, testMessageList.size() - oldSize);
    }


    @Test
    void pictureNotExists() {
        Message m = testMessageList.get(0);
        assertNull(m.getPictureUrl());
        assertNotEquals(ConfigTests.DEFAULT_PICTURE_URI, m.getPictureUrl());
    }

    @Test
    void pictureExists() {
        Message m = testMessageList.get(1);
        assertNotNull(m.getPictureUrl());
        assertNotEquals(ConfigTests.DEFAULT_PICTURE_URI, testMessage.getPictureUrl());
    }

}