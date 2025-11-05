package pn.back.cfg;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pn.back.entities.Comment;
import pn.back.services.CommentService;

import java.util.ArrayList;
import java.util.List;

import static pn.back.cfg.MessageTestConfig.DEFAULT_CONTENT;

@TestConfiguration
public class CommentsTestConfig {


    @Bean
    Comment testComment() {
        return new Comment(DEFAULT_CONTENT + "-TEST");
    }


    @Bean
    List<Comment> testCommentList() {
        List<Comment> commentListResult = new ArrayList<>();
        commentListResult.add(new Comment(1L, DEFAULT_CONTENT + 1, 1L));
        commentListResult.add(new Comment(2L, DEFAULT_CONTENT + 2, 1L));
        commentListResult.add(new Comment(3L, DEFAULT_CONTENT + 3, 1L));
        commentListResult.add(new Comment(4L, DEFAULT_CONTENT, 2L));
        commentListResult.add(new Comment(5L, DEFAULT_CONTENT, 2L));
        commentListResult.add(new Comment(6L, DEFAULT_CONTENT + 4, 1L));
        commentListResult.add(new Comment(7L, DEFAULT_CONTENT, 2L));
        commentListResult.add(new Comment(8L, DEFAULT_CONTENT, 2L));
        commentListResult.add(new Comment(9L, DEFAULT_CONTENT, 2L));

        return commentListResult;
    }

    @Bean
    CommentService cmService() {
        return new CommentService();
    }
}
