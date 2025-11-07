package pn.back.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pn.back.cfg.CommentsTestConfig;
import pn.back.cfg.MessageTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@SpringBootTest
@AutoConfigureMockMvc
@Import({CommentsTestConfig.class, MessageTestConfig.class})
public class MessageControllerWithDBTest {


    Connection connection;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private MockMvc mockMvc;
    private PreparedStatement prs;

    @BeforeEach
    void init() throws SQLException {
        connection = jdbcTemplate.getDataSource().getConnection();

        clearMessages();
        createMessages();
    }

    @Test
    void info() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello0"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void showAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void showById() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", postID))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void seshowAllPG() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
                        .param("pageNumber", "0")
                        .param("pageSize", "3")
                        .param("search", "T")

                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void editComment() throws Exception {
        long postID = getIdBetween();
        String requestBody =
                " { \"content\": \"Comment: ABC  " + postID + "\" }";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void editTitle() throws Exception {
        long postID = getIdBetween();
        String requestBody =
                " { \"title\": \"Title: ABC  " + postID + "\" }";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void editContentTitle() throws Exception {
        long postID = getIdBetween();
        String requestBody =
                " { \"title\": \"Title: ABC  " + postID + "\" , " +
                        "  \"content\": \"Comment: ABC  " + postID + "\"  }";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void incrementLikes() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{id}/likes", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void incrementComments() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{id}/comments-count", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addNewMessageTitle() throws Exception {
        long postID = (long) (Math.random() * System.currentTimeMillis());
        String requestBody =
                " { \"title\": \"Title: ABC  " + postID + "\" }";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    void addNewMessageContent() throws Exception {
        long postID = (long) (Math.random() * System.currentTimeMillis());
        String requestBody =
                " { \"content\": \"Comment: ABC  " + postID + "\" }";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void addNewMessageContentTitle() throws Exception {
        long postID0 = (long) (Math.random() * System.currentTimeMillis());
        long postID1 = (long) (Math.random() * System.currentTimeMillis());
        String requestBody =
                " { \"title\": \"Title: ABC  " + postID0 + "\" , " +
                        "  \"content\": \"Comment: ABC  " + postID1 + "\"  }";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void deletePost() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", postID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
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
