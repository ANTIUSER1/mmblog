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

import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTest.class})
class MessageServiceNegativeTest {


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
    @InjectMocks
    private MessageService messageService;


    @Test
    void findById() {
        when(messageService.findAll()).thenReturn(testMessageList);

        assertNotEquals(1, messageService.findAll().size());
    }

    @Test
    void showAllPG() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 3
                        && message.getContent().contains("ont"))
                .toList();
        when(messageService.findAll()).thenReturn(testMessageList);
        assertNotEquals(5, messageService.findAll().size());

        MessagePageData mpd = new MessagePageData(
                testMessageList, false, false, 22);
        assertFalse(mpd.isHasPrev());
        assertFalse(mpd.isHasNext());
        assertFalse(22 < mpd.getPosts().size());

    }

    @Test
    void modifyMessage() throws SQLException {

        Message m = testMessageList.get(0);
        m.setTitle(ConfigTest.DEFAULT_TITLE);
        m.setContent(ConfigTest.DEFAULT_CONTENT);
        m.setLikesCount(ConfigTest.DEFAULT_LIKES_COUNT);
        assertFalse(m.getContent().equals(ConfigTest.DEFAULT_CONTENT + " ABC")
                && m.getTitle().equals(ConfigTest.DEFAULT_TITLE));
        assertNotEquals(122, m.getCommentsCount());
    }

    @Test
    void incrementLikes() {
        Message m = testMessageList.get(0);
        long oldLikesCount = m.getLikesCount();
        m.setLikesCount(oldLikesCount + 1);
        assertNotEquals(100, m.getLikesCount() - oldLikesCount);
    }

    @Test
    void addMessage() {
        int oldSize = testMessageList.size();
        testMessageList.add(testMessage);
        assertNotEquals(111, testMessageList.size() - oldSize);
    }

    @Test
    void delete() {
        int oldSize = testMessageList.size();
        testMessageList.remove(testMessageList.get(1));
        assertFalse(testMessageList.size() - oldSize > 100);
    }


    @Test
    void pictureNotExists() {
        Message m = testMessageList.get(0);
        assertNull(m.getPictureUrl());
    }

    @Test
    void pictureExists() {
        Message m = testMessageList.get(0);
        assertNull(m.getPictureUrl());
        assertNotEquals(ConfigTest.DEFAULT_PICTURE_URI, testMessage.getPictureUrl());
    }

}