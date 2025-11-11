package pn.back.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Message;
import pn.back.entities.MessagePageData;
import pn.back.repo.MessageRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest

@ContextConfiguration(classes = {MessageTestConfig.class})
class MessageServiceTest {

    @Autowired
    private List<Message> testMessageList;

    @Autowired
    private Message testMessage;

    @MockitoBean
    private MessageRepository msgService;


    @Test
    void findById() {
        testMessageList = testMessageList
                .stream()
                .filter(m -> m.getId() == 1).toList();
        when(msgService.findAll()).thenReturn(testMessageList);
        Assertions.assertEquals(1, msgService.findAll().size());
    }

    @Test
    void findAll() {
        //   List<Message> allMSG = msgService.findAll();
        when(msgService.findAll()).thenReturn(testMessageList);
        System.out.println(msgService.findAll() == null);
        for (Message m : msgService.findAll()) {
            System.out.println(m);
        }
        Assertions.assertEquals(2, msgService.findAll().size());
    }

    @Test
    void showAllPG() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 3
                        && message.getContent().contains("ont"))
                .toList();
        for (Message mm : testMessageList) {
            System.out.println(mm);
        }
        when(msgService.findAll()).thenReturn(testMessageList);
        Assertions.assertEquals(2, msgService.findAll().size());

        MessagePageData mpd = new MessagePageData(
                testMessageList, false, true, 22);
        Assertions.assertTrue(mpd.isHasPrev());
        Assertions.assertFalse(mpd.isHasNext());
        Assertions.assertEquals(2, mpd.getPosts().size());

    }


    @Test
    void modifyMessage() {
        Message m = testMessageList.get(0);
        m.setTitle(MessageTestConfig.DEFAULT_TITLE);
        m.setContent(MessageTestConfig.DEFAULT_CONTENT);
        m.setLikesCount(MessageTestConfig.DEFAULT_LIKES_COUNT);
        Assertions.assertTrue(m.getContent().equals(MessageTestConfig.DEFAULT_CONTENT)
                && m.getTitle().equals(MessageTestConfig.DEFAULT_TITLE));
        Assertions.assertTrue(m.getCommentsCount() != MessageTestConfig.DEFAULT_COMMENTS_COUNT);
    }


    @Test
    void incrementLikes() {
        Message m = testMessageList.get(0);
        long oldLikesCount = m.getLikesCount();
        m.setLikesCount(oldLikesCount + 1);
        Assertions.assertEquals(1, m.getLikesCount() - oldLikesCount);
    }

    @Test
    void addMessage() {
        int oldSize = testMessageList.size();
        testMessageList.add(testMessage);
        Assertions.assertEquals(1, testMessageList.size() - oldSize);
    }

    @Test
    void delete() {
        int oldSize = testMessageList.size();
        testMessageList.remove(testMessageList.get(1));
        Assertions.assertEquals(-1, testMessageList.size() - oldSize);
    }


    @Test
    void pictureExists() {
        Message m = testMessageList.get(2);
        Assertions.assertNotNull(m.getPictureUrl());
        System.out.println(m);
        // Assertions.assertNotEquals(MessageTestConfig.DEFAULT_PICTURE_URI, testMessage.getPictureUrl());
    }
}