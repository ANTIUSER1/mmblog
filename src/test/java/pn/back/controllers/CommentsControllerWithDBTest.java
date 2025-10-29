package pn.back.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pn.back.config.ConfigTest;
import pn.back.config.DBConfig;
import pn.back.config.WebConfigTest;
import pn.back.entities.Message;
import pn.back.mappers.MessageMapper;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static pn.back.repo.MessageRepositoryImpl.MAIN_SQL_TEST_SELECT;

@SpringJUnitConfig(classes = {
        DBConfig.class,
        ConfigTest.class,
        WebConfigTest.class
})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentsControllerWithDBTest {

    private static final int MAX_SIMPLE_MSG = 5;
    private static final int MAX_TAG_MSG = 8;

    @Autowired
    Connection connection;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MessageMapper messageMapper;
    private MockMvc mockMvc;
    private PreparedStatement prs;

    @BeforeEach
    public void init() throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        messageMapper = new MessageMapper();
       /*
        String sql1 = "DELETE FROM blog_test.messages";
        prs = connection.prepareStatement(sql1);
        prs.execute();
        String sql12 = "DELETE FROM blog_test.comments";
        prs = connection.prepareStatement(sql12);
        prs.execute();

        createTestMessages();
        createTestComments();
        */
    }

    @Test
    void info() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getCommentsForPost() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/" + postID + "/comments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(4)))
        ;
    }

    @Test
    void getCommenByNumberForPost() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/" + postID + "/comments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))

        ;
    }

    @Test
    void editCommentsForPost() throws Exception {
        long postID = getIdBetween();
        String requestBody =
                " { \"content\": \"00t\" " +
                        "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/" + postID + "/comments/2")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3))
                )
        ;

    }

    @Test
    void addCommentsForPost() throws Exception {
        long postID = getIdBetween();
        String requestBody =
                " { \"content\": \"ABC\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/" + postID + "/comments")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("ABC"))
        ;
    }

    private long getIdBetween() {
        long min = jdbcTemplate.queryForObject("SELECT MIN(id) FROM  messages  ", Long.class);
        long max = jdbcTemplate.queryForObject("SELECT MAX(id) FROM  messages  ", Long.class);
        double r = Math.random();
        return (long) (min * r + (1 - r) * max);
    }

    private void createTestComments() throws SQLException {
        long min = jdbcTemplate.queryForObject("SELECT MIN(id) FROM  blog_test.messages  ", Long.class);
        long max = jdbcTemplate.queryForObject("SELECT MAX(id) FROM  blog_test.messages  ", Long.class);
        String sql =
                "INSERT INTO blog_test.comments " +
                        " (  content , message_key ) " +
                        " VALUES (  ? ,  ?  ) ";
        for (long k = min; k <= max; k++) {
            for (int n = 1; n <= 3; n++) {
                incrCommentCount(k);
                prs = connection.prepareStatement(sql);
                prs.setString(1, "Comment-" + n + "-for-post-" + k);
                prs.setLong(2, k);
                prs.executeUpdate();
            }
        }
    }

    private void incrCommentCount(long k) throws SQLException {
        String sql0 = MAIN_SQL_TEST_SELECT + " WHERE id =? ";

        List<Message> resultList =
                jdbcTemplate.query(sql0,
                        new PreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps) throws SQLException {
                                ps.setLong(1, k);
                            }
                        },
                        messageMapper);

        if (!resultList.isEmpty()) {
            long cc = resultList.get(0).getCommentsCount() + 1;
            String sql = "    UPDATE blog_test.messages  " +
                    "             SET  " +
                    "                  comments_count = ? " +
                    "     WHERE id = ? ";
            prs = connection.prepareStatement(sql);
            prs.setLong(1, cc);
            prs.setLong(2, k);
            prs.executeUpdate();
        }
    }

    private void createTestMessages() throws SQLException {
        String sql2 =
                "INSERT INTO blog_test.messages " +
                        " (title, content  ) " +
                        " VALUES (  ? ,  ?  ) ";
        String sql21 =
                "INSERT INTO blog_test.messages " +
                        " (title, content  , tags) " +
                        " VALUES (  ? ,  ? , ? ) ";

        prs = connection.prepareStatement(sql2);
        for (int k = 1; k < MAX_SIMPLE_MSG; k++) {
            prs.setString(1, "Title-" + k);
            prs.setString(2, "Content-" + k);
            prs.executeUpdate();
        }

        prs = connection.prepareStatement(sql21);
        for (int k = 1; k < MAX_TAG_MSG; k++) {
            try {
                String[] tags = {"tag1-" + k, "tag2-" + k, "tag3-" + k};
                Array sqlArray = connection.createArrayOf("TEXT", tags);

                prs.setString(1, "Title-With-Tags-" + k);
                prs.setString(2, "Content-With-Tags-" + k);
                prs.setArray(3, sqlArray);
                prs.executeUpdate();
            } catch (SQLException e) {
                System.out.println("\n ERROR " + e.getMessage());
            }
        }
    }
}