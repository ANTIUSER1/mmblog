package pn.back.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pn.back.config.ConfigTest;
import pn.back.config.DBConfig;
import pn.back.config.WebConfigTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringJUnitConfig(classes = {
        DBConfig.class,
        ConfigTest.class,
        WebConfigTest.class
})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
public class PictureControllerTest {

    @Autowired
    PictureController pictureController;

    @Autowired
    Connection connection;

    @Autowired
    JdbcTemplate jdbcTemplate;
    long postID = 0;
    private MockMvc mockMvc;
    private PreparedStatement prs;

    // @BeforeEach
    void init() throws SQLException {
        mockMvc = MockMvcBuilders.standaloneSetup(pictureController).build();
        System.out.println("SCHEMA:: " + connection.getSchema());
        clearMessages();
        createMessages();
    }

    @Test
    void addPicture() throws Exception {
        init();
        postID = getIdBetween();

        MockMultipartFile file = new MockMultipartFile("file", "dummy.csv",
                "text/plain", "Some dataset...".getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/posts/" + postID + "/image");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    void getPicture() throws Exception {
        addPicture();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}/image", postID))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    //**********************************************************

    private long getIdBetween() {
        try {
            long min = jdbcTemplate.queryForObject("SELECT MIN(id) FROM  messages  ", Long.class);
            long max = jdbcTemplate.queryForObject("SELECT MAX(id) FROM  messages  ", Long.class);
            double r = Math.random();
            return (long) (min * r + (1 - r) * max);
        } catch (Exception e) {
            return 0;
        }
    }

    private void clearMessages() throws SQLException {
        String sql = " DELETE FROM messages ";
        prs = connection.prepareStatement(sql);
        prs.execute();
    }

    private void createMessages() throws SQLException {
        for (long k = 0; k < 20; k++) {
            String sql = " INSERT INTO messages " +
                    " ( title, content, likes_count, comments_count )  " +
                    "   VALUES  ( ?,  ?,  ?,  ? )  ";

            prs = connection.prepareStatement(sql);
            prs.setString(1, "Title-" + k);
            prs.setString(2, "Cont---" + k);
            prs.setLong(3, (1) % 4);
            prs.setLong(4, k % 3);
            prs.executeUpdate();
        }

    }
}
