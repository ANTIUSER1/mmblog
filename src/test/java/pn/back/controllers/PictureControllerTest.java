package pn.back.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pn.back.config.WebConfigTest;
import pn.back.services.MessageService;
import pn.back.utils.ControllerUtil;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {WebConfigTest.class})
@WebAppConfiguration
@EnableWebMvc
class PictureControllerTest {

    @Autowired
    MultipartFile multipartFile;

    MockMvc mockMvc;


    @InjectMocks
    PictureController pictureController;

    @Mock
    private MessageService messageService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(pictureController).build();
    }


    @Test
    void addPicture() throws Exception {
        String url = ControllerUtil.ALL_POSTS_API + "/{id}/image";
        mockMvc.perform(MockMvcRequestBuilders.multipart(url, 14).file((MockMultipartFile) multipartFile))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void getPicture() throws Exception {
        String url = ControllerUtil.ALL_POSTS_API + "/{id}/image";
        mockMvc.perform(MockMvcRequestBuilders.get(url, 14))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}