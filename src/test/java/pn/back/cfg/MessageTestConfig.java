package pn.back.cfg;

import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pn.back.entities.Message;
import pn.back.services.MessageService;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class MessageTestConfig {


    public static final String DEFAULT_TITLE = "Default title";
    public static final String DEFAULT_CONTENT = "Default content";
    public static final String DEFAULT_PICTURE_URI = "/picture";
    public static final long DEFAULT_LIKES_COUNT = 3;
    public static final long DEFAULT_COMMENTS_COUNT = 7;


    @Bean
    Message testMessage() {
        return new Message(1000, DEFAULT_TITLE,
                DEFAULT_CONTENT, DEFAULT_LIKES_COUNT, DEFAULT_COMMENTS_COUNT,
                null);

    }

    @Bean
    Message testMessageWithPicture() {
        return new Message(0, DEFAULT_TITLE,
                DEFAULT_CONTENT, DEFAULT_LIKES_COUNT, DEFAULT_COMMENTS_COUNT,
                DEFAULT_PICTURE_URI);

    }

    @Bean
    List<Message> testMessageList() {
        List<Message> messageListresult = new ArrayList<>();

        messageListresult.add(new Message(1L, "title1",
                "content1", 10, 19,
                null));
        String[] tags = {"dd", "aa"};
        messageListresult.add(new Message(2L, "title2",
                "content2", 100, 1,
                null, tags));

        messageListresult.add(new Message(3L, "title3",
                "content3", 8, 15,
                DEFAULT_PICTURE_URI));

        return messageListresult;
    }

    @Bean
    @Order(0)
    MessageService msgService() {
        return new MessageService();
    }
}
