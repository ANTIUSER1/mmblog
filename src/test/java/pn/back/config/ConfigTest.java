package pn.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import({pn.back.utils.ControllerUtil.class})
public class ConfigTest {

    public static final int MAX_SIMPLE_MSG = 8;
    public static final int MAX_TAG_MSG = 8;

    public static final String DEFAULT_TITLE = "Default title";
    public static final String DEFAULT_CONTENT = "Default content";
    public static final String DEFAULT_PICTURE_URI = "/picture";
    public static final long DEFAULT_LIKES_COUNT = 3;
    public static final long DEFAULT_COMMENTS_COUNT = 7;

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
    List<Comment> testCommentList() {
        List<Comment> commentListResult = new ArrayList<>();
        commentListResult.add(new Comment(1L, DEFAULT_CONTENT, 1L));
        commentListResult.add(new Comment(2L, DEFAULT_CONTENT, 1L));
        commentListResult.add(new Comment(3L, DEFAULT_CONTENT, 1L));
        commentListResult.add(new Comment(4L, DEFAULT_CONTENT, 2L));
        commentListResult.add(new Comment(5L, DEFAULT_CONTENT, 2L));

        return commentListResult;
    }

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
    Comment testComment() {
        return new Comment(DEFAULT_CONTENT + "-TEST");
    }


}
