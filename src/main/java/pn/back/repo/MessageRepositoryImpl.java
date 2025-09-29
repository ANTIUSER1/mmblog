package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Message;
import pn.back.mappers.MessageMapper;

import java.util.List;

@Repository
@Transactional
@Slf4j
public class MessageRepositoryImpl implements MessageRepository {

    private static final String MAIN_SQL = "  SELECT * FROM pract.blog.messages   ";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MessageMapper messageMapper;


    //  @Override
    public Message findById(long id) {

        return jdbcTemplate.queryForObject(
                MAIN_SQL + " WHERE id = " + id,
                messageMapper
        );
    }

    @Override
    public List<Message> findAll() {
        Message m = findById(1);
        log.info("\n MESSAGE :  {}\n", m);
        log.info("\n NUMBER :  {}\n ", numberOfRecords("WW"));
        return jdbcTemplate.query(
                MAIN_SQL + "  ORDER BY id asc",
                messageMapper);


    }

    @Override
    public long numberOfRecords(String search) {
        return jdbcTemplate.query(
                MAIN_SQL +
                        " WHERE title like '%" + search + "%'"
                        + " OR content like '%" + search + "%'"
                        + "  ORDER BY id asc",
                messageMapper).size();

    }

    @Override
    public List<Message> showAllByPage(int page, int limit, String search) {
        return List.of();
    }




    /*

     */
}
