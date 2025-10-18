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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTest.class})
public class MMMessageServiceTest {


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
        assertEquals(3, messageService.findAll().size());
    }

    @Test
    void findByIdInRepo() {
        when(messageRepository.findAll()).thenReturn(testMessageList);
        assertEquals(3, messageRepository.findAll().size());
    }

    @Test
    void showAllPages() {
        testMessageList = testMessageList.stream()
                .filter(message -> message.getId() < 3
                        && message.getContent().contains("ont"))
                .toList();
        when(messageService.findAll()).thenReturn(testMessageList);
        assertEquals(1, messageService.findAll().size());

        MessagePageData mpd = new MessagePageData(
                testMessageList, false, true, 22);
        assertTrue(mpd.isHasPrev());
        assertFalse(mpd.isHasNext());
        assertEquals(2, mpd.getPosts().size());
    }

}
