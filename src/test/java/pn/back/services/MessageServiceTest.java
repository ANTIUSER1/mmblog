package pn.back.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.back.config.ConfigTest;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.entities.MessagePageData;
import pn.back.repo.MessageRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTest.class})
class MessageServiceTest {


    @Autowired
    private List<Comment> testCommentList;
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
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 3
                        && message.getContent().contains("ont"))
                .toList();
        when(messageService.findAll()).thenReturn(testMessageList);
        assertEquals(2, messageService.findAll().size());

        MessagePageData mpd = new MessagePageData(
                testMessageList, false, true, 22);
        assertTrue(mpd.isHasPrev());
        assertFalse(mpd.isHasNext());
        assertEquals(2, mpd.getPosts().size());

    }

    @Test
    void modifyMessage() {
        Message m = testMessageList.get(0);
        m.setTitle(ConfigTest.DEFAULT_TITLE);
        m.setContent(ConfigTest.DEFAULT_CONTENT);
        m.setLikesCount(ConfigTest.DEFAULT_LIKES_COUNT);
        assertTrue(m.getContent().equals(ConfigTest.DEFAULT_CONTENT) && m.getTitle().equals(ConfigTest.DEFAULT_TITLE));
        assertTrue(m.getCommentsCount() != ConfigTest.DEFAULT_COMMENTS_COUNT);
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
        assertNotEquals(ConfigTest.DEFAULT_PICTURE_URI, m.getPictureUrl());
    }

    @Test
    void pictureExists() {
        Message m = testMessageList.get(1);
        assertNotNull(m.getPictureUrl());
        assertNotEquals(ConfigTest.DEFAULT_PICTURE_URI, testMessage.getPictureUrl());
    }

}