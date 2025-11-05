package pn.back.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pn.back.cfg.MessageConfig;
import pn.back.entities.Message;
import pn.back.repo.MessageRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@Import({MessageConfig.class})
class MessageServiceTest {

    @Autowired
    private List<Message> testMessageList;
    @Mock
    private MessageRepository msgService;


    @Test
    void findAll() {
        //   List<Message> allMSG = msgService.findAll();
        when(msgService.findAll()).thenReturn(testMessageList);
        System.out.println(msgService.findAll() == null);
        for (Message m : msgService.findAll()) {
            System.out.println(m);
        }
        Assertions.assertEquals(3, msgService.findAll().size());
    }
}