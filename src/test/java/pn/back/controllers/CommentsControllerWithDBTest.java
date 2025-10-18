package pn.back.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import pn.back.config.ConfigTest;
import pn.back.config.DBConfig;
import pn.back.entities.Message;
import pn.back.mappers.MessageMapper;
import pn.back.services.CommentService;
import pn.back.utils.ControllerUtil;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pn.back.repo.MessageRepositoryImpl.MAIN_SQL_SELECT;
import static pn.back.repo.MessageRepositoryImpl.MAIN_SQL_TEST_SELECT;

@SpringJUnitConfig(classes = {
        DBConfig.class,
        ConfigTest.class,
})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentsControllerWithDBTest {


    private static final int MAX_SIMPLE_MSG = 5;
    private static final int MAX_TAG_MSG = 8;

    RestTemplate restTemplate;

    @Autowired
    Connection connection;
    @InjectMocks
    CommentService commentService;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //**************************************************
    //@InjectMocks
    private MessageMapper messageMapper;
    //*************************************
    private MockMvc mockMvc;
    private PreparedStatement prs;

    @BeforeEach
    public void init() throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        messageMapper = new MessageMapper();
        String sql1 = "DELETE FROM pract.blog_test.messages";
        prs = connection.prepareStatement(sql1);
        prs.execute();
        String sql12 = "DELETE FROM pract.blog_test.comments";
        prs = connection.prepareStatement(sql12);
        prs.execute();

        createTestMessages();
        createTestComments();
    }


    @Test
    void info() throws Exception {
        String sql = MAIN_SQL_SELECT;
        System.out.println(connection.getSchema());
        mockMvc.perform(get("http://192.168.2.100:8080/mmblog/hello"))
                .andDo(MockMvcResultHandlers.print())
        ;

//        restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "http://192.168.2.100:8080/mmblog/hello",
//                String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Hello, World!", response.getBody());
//        System.out.println(response.getStatusCode());
//        System.out.println(response.getBody());
        //      Thread.sleep(80000);
    }

    @Test
    void getCommentsForPost() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(get(
                        ControllerUtil.ALL_POSTS_API + "/" + postID + "/comments"))
                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(
//                        jsonPath("$", hasSize(4)))
//                .andExpect(
//                        jsonPath(
//                                "$[0].messageKey").value(4))
                .andDo(MockMvcResultHandlers.print())

        ;
        Thread.sleep(80000);
    }

    @Test
    void getCommenByNumberForPost() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(get(
                        ControllerUtil.ALL_POSTS_API + "/" + postID + "/comments/2"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(
//                        jsonPath("$", hasSize(4)))
//                .andExpect(
//                        jsonPath(
//                                "$[0].messageKey").value(4))
                .andDo(MockMvcResultHandlers.print())
        ;
        Thread.sleep(80000);
    }

    @Test
    void editCommentsForPost() throws Exception {
        long postID = getIdBetween();
        mockMvc.perform(put(
                        ControllerUtil.ALL_POSTS_API + "/" + postID + "/comments/3"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(
//                        jsonPath("$", hasSize(4)))
//                .andExpect(
//                        jsonPath(
//                                "$[0].messageKey").value(4))
                .andDo(MockMvcResultHandlers.print())

        ;
        Thread.sleep(80000);
    }


//**************************************************************
//**************************************************************
//**************************************************************

    private long getIdBetween() {
        long min = jdbcTemplate.queryForObject("SELECT MIN(id) FROM  pract.blog_test.messages  ", Long.class);
        long max = jdbcTemplate.queryForObject("SELECT MAX(id) FROM  pract.blog_test.messages  ", Long.class);
        double r = Math.random();
        return (long) (min * r + (1 - r) * max);
    }

    private void createTestComments() throws SQLException {
        long min = jdbcTemplate.queryForObject("SELECT MIN(id) FROM  pract.blog_test.messages  ", Long.class);
        long max = jdbcTemplate.queryForObject("SELECT MAX(id) FROM  pract.blog_test.messages  ", Long.class);
        //  System.out.println(" MIN " + min + "  MAX " + max);
        String sql =
                "INSERT INTO pract.blog_test.comments " +
                        " (  content , message_key ) " +
                        " VALUES (  ? ,  ?  ) ";
        //System.out.println(" SQL INS " + sql);
        for (long k = min; k < max; k++) {
            for (int n = 0; n < 4; n++) {
                incrCommentCount(k);
                prs = connection.prepareStatement(sql);
                // System.out.println(k + " /  " + n + "  PRS  Comments--- :" + prs);
                prs.setString(1, " CCC -" + n);
                prs.setLong(2, k);
                //    System.out.println("PRS Comments :" + prs + "\n " + sql);
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
                                // System.out.println("  COUNT ::: " + ps);
                                //  ps.execute();
                            }
                        },
                        messageMapper);

        //  System.out.println("RES SIZE: " + resultList.size());
        long cc = resultList.get(0).getCommentsCount() + 1;
        // System.out.println("\t\t   CC " + cc);


        String sql = "    UPDATE pract.blog_test.messages  " +
                "             SET  " +
                "                  comments_count = ? " +
                "     WHERE id = ? ";
        prs = connection.prepareStatement(sql);
        prs.setLong(1, cc);
        prs.setLong(2, k);
        //  System.out.println(" CC PRS " + prs);
        prs.executeUpdate();

    }

    private void createTestMessages() throws SQLException {
        String sql2 =
                "INSERT INTO pract.blog_test.messages " +
                        " (title, content  ) " +
                        " VALUES (  ? ,  ?  ) ";
        String sql21 =
                "INSERT INTO pract.blog_test.messages " +
                        " (title, content  , tags) " +
                        " VALUES (  ? ,  ? , ? ) ";

        prs = connection.prepareStatement(sql2);
        for (int k = 1; k < MAX_SIMPLE_MSG; k++) {
            prs.setString(1, "T-" + k);
            prs.setString(2, "C-" + k);
//            System.out.println("\nPRS Messages :\n " + prs);
//            System.out.println(k + "  " + prs.executeUpdate());
        }

        prs = connection.prepareStatement(sql21);
        for (int k = 1; k < MAX_TAG_MSG; k++) {
            try {
                String[] tags = {"t-1-" + k, "t-2-" + k, "t-3-" + k};
                Array sqlArray = connection.createArrayOf("TEXT", tags);

                prs.setString(1, "T-T-" + k);
                prs.setString(2, "C-C-" + k);
                prs.setArray(3, sqlArray);
                //     System.out.println("\nPRS Messages TAGS:\n " + prs);
                prs.executeUpdate();
            } catch (SQLException e) {
                System.out.println("\n ERROR " + e.getMessage());
                //   throw new RuntimeException(e);
            }
        }
    }


}
