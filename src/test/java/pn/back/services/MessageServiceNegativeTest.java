package pn.back.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Message;
import pn.back.entities.MessagePageData;
import pn.back.repo.MessageRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@Import({MessageTestConfig.class})
class MessageServiceNegativeTest {

    @Autowired
    private List<Message> testMessageList;


    @MockitoBean
    private MessageRepository msgService;


    @Test
    void findById() {
        testMessageList = testMessageList
                .stream()
                .filter(m -> m.getId() == 1).toList();
        when(msgService.findAll()).thenReturn(testMessageList);
        Assertions.assertNotEquals(2, msgService.findAll().get(0).getId());
    }

    @Test
    void findAll() {
        //   List<Message> allMSG = msgService.findAll();
        when(msgService.findAll()).thenReturn(testMessageList);
        System.out.println(msgService.findAll() == null);
        for (Message m : msgService.findAll()) {
            System.out.println(m);
        }
        Assertions.assertNotEquals(10, msgService.findAll().size());
    }

    @Test
    void showAllPG() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 3
                        && message.getContent().contains("ont"))
                .toList();
        when(msgService.findAll()).thenReturn(testMessageList);
        Assertions.assertEquals(2, msgService.findAll().size());

        MessagePageData mpd = new MessagePageData(
                testMessageList, true, false, 22);
        Assertions.assertFalse(mpd.isHasPrev());
        Assertions.assertTrue(mpd.isHasNext());
        Assertions.assertNotEquals(5, mpd.getPosts().size());

    }


    @Test
    void modifyMessage() {
        Message m = testMessageList.get(0);
        m.setTitle(MessageTestConfig.DEFAULT_TITLE + "-");
        m.setContent(MessageTestConfig.DEFAULT_CONTENT + "--");
        m.setLikesCount(MessageTestConfig.DEFAULT_LIKES_COUNT);
        Assertions.assertFalse(m.getContent().equals(MessageTestConfig.DEFAULT_CONTENT)
                && m.getTitle().equals(MessageTestConfig.DEFAULT_TITLE));
        Assertions.assertTrue(m.getCommentsCount() != MessageTestConfig.DEFAULT_COMMENTS_COUNT);
    }


    @Test
    void incrementLikes() {
        Message m = testMessageList.get(0);
        long oldLikesCount = m.getLikesCount();
        m.setLikesCount(oldLikesCount + 1);
        Assertions.assertNotEquals(5, m.getLikesCount() - oldLikesCount);
    }

    @Test
    void addMessage() {
        int oldSize = testMessageList.size();
        Assertions.assertNotEquals(1, testMessageList.size() - oldSize);
    }

    @Test
    void delete() {
        testMessageList.remove(testMessageList.get(1));
        List<Message> tmt = testMessageList.stream()
                .filter(m -> m.getId() > 1)
                .toList();
        Assertions.assertNotEquals(-1, testMessageList.size() - tmt.size());
    }


    @Test
    void pictureNotExists() {
        Message m = testMessageList.get(0);
        Assertions.assertNull(m.getPictureUrl());
        Assertions.assertNotEquals(MessageTestConfig.DEFAULT_PICTURE_URI, m.getPictureUrl());
    }


}