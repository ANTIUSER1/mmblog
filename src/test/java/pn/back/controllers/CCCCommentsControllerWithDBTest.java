package pn.back.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import pn.back.config.ConfigTest;
import pn.back.config.DBConfig;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.mappers.MessageMapper;
import pn.back.services.CommentService;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pn.back.repo.MessageRepositoryImpl.MAIN_SQL_TEST_SELECT;

@SpringJUnitConfig(classes = {
        DBConfig.class,
        ConfigTest.class,
})
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
public class CCCCommentsControllerWithDBTest {

    private static final int MAX_SIMPLE_MSG = 5;
    private static final int MAX_TAG_MSG = 8;


    @Autowired
    Connection connection;
    @InjectMocks
    CommentService commentService;
    ResponseEntity<String> response;
    @InjectMocks
    CommentsController commentsController;

    @Autowired
    List<Comment> testCommentList;
    @Autowired
    Comment testComment;
    @Autowired
    List<Message> testMessageList;
    @Autowired
    Message testMessage;
    @Value("${host}")
    private String host;
    //    @Autowired
//    private WebApplicationContext wac;
    //**************************************************
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private MessageMapper messageMapper;
    //*************************************

    //@Mock
    private RestTemplate restTemplate;


    private MockMvc mockMvc;
    private PreparedStatement prs;

    @BeforeEach
    public void init() throws SQLException {
        restTemplate = Mockito.mock(RestTemplate.class);

        // restTemplate = new RestTemplate();
        host = "http://" + host;
        mockMvc = MockMvcBuilders.standaloneSetup(commentsController).build();
////           mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String sql1 = "DELETE FROM pract.blog_test.messages";
//        prs = connection.prepareStatement(sql1);
//        prs.execute();
//        String sql12 = "DELETE FROM pract.blog_test.comments";
//        prs = connection.prepareStatement(sql12);
//        prs.execute();
//
//        createTestMessages();
//        createTestComments();
    }


    @Test
    void info() throws Exception {

        System.out.println("      RT " + restTemplate);
//        String url = host + "/mmblog/api/posts/22/comments";
        String url = host + "/mmblog/hello";
        System.out.println("URL " + url);


        Message m = new Message(

                100L, " NNN ", " CC CC CC ", 1025L, 555L, null);
        Mockito.when(restTemplate.getForEntity(
                url, Message.class)).thenReturn(
                new ResponseEntity<Message>(m, HttpStatus.OK)
        );

        System.out.println(m);
        System.out.println("   MOCK:::" +
                restTemplate.getForEntity(url, Message.class)

        );

//        String sql = MAIN_SQL_SELECT;
//        System.out.println(messageMapper);
//        prs = connection.prepareStatement(sql);
//        response = restTemplate.getForEntity(
//                host + "/mmblog/hello",
//                String.class);
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getCommentsForPost() throws Exception {
        String url = "/api/posts/777/comments";
//        String url = host + "/mmblog/api/posts/777/comments";
        Mockito.when(restTemplate.getForEntity(
                url, List.class)).thenReturn(
                new ResponseEntity<List>(testCommentList, HttpStatus.OK)
        );
        List<Comment> lk = restTemplate.getForEntity(
                url, List.class).getBody();
        Assertions.assertTrue(lk.size() > 2);
        Assertions.assertSame(restTemplate.getForEntity(
                url, List.class).getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getCommenByNumberForPost() {
        String url = "/api/posts/777/comments/3";
        Mockito.when(restTemplate.getForEntity(
                url, Comment.class)).thenReturn(
                new ResponseEntity<Comment>(testComment, HttpStatus.OK)
        );
        Assertions.assertSame(restTemplate.getForEntity(
                url, Comment.class).getStatusCode(), HttpStatus.OK);
    }

    @Test
    void editCommentsForPost() {
        String url = "/api/posts/777/comments/3";
        String requestBody = " {\n" +
                "    \"id\": 67,\n" +
                "    \"content\": \"   ee c  79 ..\",\n" +
                "    \"messageKey\": 22\n" +
                "}";
//        String requestBody = "Updated Data";
        restTemplate.put(url, requestBody);
        verify(restTemplate, times(1))
                .put(url, requestBody);
    }

    @Test
    void addCommentsForPost() throws Exception {
        System.out.println("TC: " + testComment);
        System.out.println("CC: " + commentsController);
        System.out.println("HOST: " + host);
        String url = host + "/mmblog/api/posts/22/comments";
        System.out.println(url);
        String requestBody = " {\n" +
                "    \"id\": 67,\n" +
                "    \"content\": \"   ee c  79 ..\",\n" +
                "    \"messageKey\": 22\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(url, testComment))
                .andExpect(MockMvcResultMatchers.status().isOk());

//        Mockito.when(commentsController.addCommentsForPost(testComment, 777));
//        //       .thenReturn(   new ResponseEntity(testComment, HttpStatus.OK));

//        System.out.println(response.getStatusCode());
//        System.out.println(response.getBody());
//        Mockito.when(restTemplate.postForEntity(
//                url, Comment.class));
//        Assertions.assertSame(restTemplate.getForEntity(
//                url, Comment.class).getStatusCode(), HttpStatus.OK);
    }

/*
    @Test
    void getCommenByNumberForPost() throws Exception {
        String url = "/api/posts/22/comments";
//        String url = host + "/mmblog/api/posts/22/comments";
        System.out.println(url);

        MvcResult mr = mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        System.out.println("RES:::  " + mr.getResponse().getStatus());
//        response = restTemplate.getForEntity(
//                url, String.class);
//
//        System.out.println(response.getStatusCode());
//        System.out.println(response.getBody());

        // Thread.sleep(30000);
    }
*/


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
