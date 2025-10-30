package pn.back.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pn.back.config.ConfigTest;
import pn.back.config.DBConfig;
import pn.back.config.WebConfigTest;
import pn.back.entities.Message;
import pn.back.mappers.MessageMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.hamcrest.Matchers.hasSize;
import static pn.back.config.ConfigTest.MAX_SIMPLE_MSG;


@SpringJUnitConfig(classes = {
        DBConfig.class,
        ConfigTest.class,
        WebConfigTest.class
})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
class MessageControllerWithDBTest {

//    @Autowired
//    private WebApplicationContext wac;


    @Autowired
    MessageController messageController;
    @Autowired
    Connection connection;
    //
    @Autowired
    Message testMessage;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //
//
    private MessageMapper messageMapper;
    private MockMvc mockMvc;
    private PreparedStatement prs;


    @BeforeEach
    void init() throws SQLException {

        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
        messageMapper = new MessageMapper();

        String sql1 = "DELETE FROM blog_test.messages";
        prs = connection.prepareStatement(sql1);
        prs.execute();
        createTestMessages();

    }


    @Test
    void info() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello0"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(MAX_SIMPLE_MSG - 1)))
        ;
    }

    @Test
    void seshowAllPG() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
                                .param("pageNumber", "0")
                                .param("pageSize", "3")
                                .param("search", "")
                        // .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(4)))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void edit() throws Exception {
        long postID = getIdBetween();
        Message m = new Message();
        m.setTitle("fr-kk");
        ObjectMapper om = new ObjectMapper();
        String requestBody = //om.writeValueAsString(m);
                " {    \"title\": \"  AB-44-55-jjjC " + postID + " \" ," +
                        " \"content\": \" 1212121 ABC " + postID + " \"  } ";
        System.out.println("\n REQUEST-EDIT::\n " + requestBody);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/" + postID)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
        ;


    }


    @Test
    void incrementCC() throws Exception {
        long postID = getIdBetween();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/" + postID + "/likes"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void addNewMessage() throws Exception {
        long postID = getIdBetween();
        String requestBody = //om.writeValueAsString(m);
                " {    \"title\": \"  AB-44-55-jjjC " + postID + " \" ," +
                        " \"content\": \" 1212121 ABC " + postID + " \"  } ";
        System.out.println("\n REQUEST-EDIT::\n " + requestBody);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        //               .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    void delete() throws Exception {
        long postID = getIdBetween();
        String requestBody = //om.writeValueAsString(m);
                " {    \"title\": \"  AB-44-55-jjjC " + postID + " \" ," +
                        " \"content\": \" 1212121 ABC " + postID + " \"  } ";
        System.out.println("\n REQUEST-EDIT::\n " + requestBody);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/" + postID))
        ;
    }

    /*

//    *******************

*/


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

    private void createTestMessages() throws SQLException {
        String sql2 =
                "INSERT INTO blog_test.messages " +
                        " (title, content  ) " +
                        " VALUES (  ? ,  ?  ) ";

        prs = connection.prepareStatement(sql2);
        for (int k = 1; k < MAX_SIMPLE_MSG; k++) {
            prs.setString(1, "Title-" + k);
            prs.setString(2, "Content-" + k);
            prs.executeUpdate();
        }
    }
}