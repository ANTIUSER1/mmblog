package pn.back.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pn.back.config.ConfigTest;
import pn.back.entities.Comment;
import pn.back.services.CommentService;
import pn.back.utils.ControllerUtil;

import java.util.List;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ConfigTest.class})
@WebAppConfiguration
@EnableWebMvc
class CommentsControllerTest {


    @Autowired
    List<Comment> testCommentList;

    MockMvc mockMvc;


    @InjectMocks
    CommentsController commentsController;

    @Mock
    private CommentService commentService;


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentsController).build();
    }

    @Test
    void getCommentsForPost() throws Exception {

        String url = ControllerUtil.ALL_POSTS_API + "/{id}/comments";
        mockMvc.perform(MockMvcRequestBuilders.get(url, 1L)
                        //        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void getCommenByNumberForPost() throws Exception {
        String url = ControllerUtil.ALL_POSTS_API + "/{id}/comments/{commentNumber}";
        mockMvc.perform(MockMvcRequestBuilders.get(url, 14, 3)
                        //        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void editCommentsForPost() throws Exception {
        String url = ControllerUtil.ALL_POSTS_API + "/{id}/comments/{commentNumber}";
        mockMvc.perform(MockMvcRequestBuilders.put(url, 14, 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" {" +
                                "          \"content\": \"2255...\"  " +
                                "  } ")
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;

    }

    @Test
    void addCommentsForPost() throws Exception {

        String url = ControllerUtil.ALL_POSTS_API + "/{id}/comments";
        mockMvc.perform(MockMvcRequestBuilders.put(url, 14)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" {" +
                                "          \"content\": \"2255...\"  " +
                                "  } ")
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())

        ;
    }
}