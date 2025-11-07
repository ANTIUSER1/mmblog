package pn.back.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pn.back.cfg.MessageTestConfig;
import pn.back.entities.Message;
import pn.back.mappers.MessageMapper;

import java.sql.Connection;
import java.util.List;

@SpringBootTest
@ContextConfiguration( classes ={MessageTestConfig.class} )
public class MsgDataTest {

    Connection connection;
    @Autowired
    MessageMapper mapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockitoBean
    private MessageRepository messageRepository;

    @Test
    void extactDataTest() {
        String sql = "SELECT * FROM messages ORDER BY id ASC ";
        List<Message> lm = jdbcTemplate.query(sql, mapper);
        for (Message mm : lm) {
            System.out.println(mm);
        }

    }
}
