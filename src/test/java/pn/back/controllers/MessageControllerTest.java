package pn.back.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pn.back.config.WebConfigTest;
import pn.back.repo.MessageRepository;
import pn.back.services.MessageService;
import pn.back.utils.ControllerUtil;


@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {WebConfigTest.class})
@WebAppConfiguration
@EnableWebMvc
class MessageControllerTest {


    MockMvc mockMvc;
    @InjectMocks
    MessageController messageController;


    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageService messageService;


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
//        mockMvc = MockMvcBuilders.standaloneSetup(new MessageController()).build();
    }

    @Test
    void findAll() throws Exception {

        String url = ControllerUtil.ALL_POSTS_API + "/all";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        // Проверка, что ответ имеет статус 200 OK
        ;
    }

    @Test
    void seshowAllPG() throws Exception {

        String url = ControllerUtil.ALL_POSTS_API;
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                                .param("pageNumber", "0")
                                .param("pageSize", "2")
                                .param("search", "")
                        // .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
        //   .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void edit() throws Exception {
        Long id = 104L;
        String url = ControllerUtil.ALL_POSTS_API + "/{id}";
        System.out.println(url);

        mockMvc.perform(MockMvcRequestBuilders.put(url, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(" {" +
                                "    \"title\": \"Название поста 3\", " +
                                "    \"text\": \"Текст поста в формате Markdown...\", " +
                                "    \"tags\": [\"tag_1\", \"tag_2\"]  " +
                                "  }")
                )
                //   .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
    }


    @Test
    void incrementCC() throws Exception {
        Long id = 104L;
        String url = ControllerUtil.ALL_POSTS_API + "/{id}/likes";
        System.out.println(url);

        mockMvc.perform(MockMvcRequestBuilders.post(url, id))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void addNewMessage() throws Exception {
        String url = ControllerUtil.ALL_POSTS_API;

        mockMvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void delete() throws Exception {

        String url = ControllerUtil.ALL_POSTS_API + "/{id}";

        mockMvc.perform(MockMvcRequestBuilders.delete(url, 12))
        ;
    }
}