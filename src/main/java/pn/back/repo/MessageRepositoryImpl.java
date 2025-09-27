package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Message;

import java.util.List;

@Repository
@Transactional
@Slf4j
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Message> findAll() {
        log.info("\n\n JDBC-jdbcTemplate--- {}", jdbcTemplate == null);
        return jdbcTemplate.query(
                " SELECT * FROM pract.blog.messages ORDER BY id asc",
                (rs, rowNum) -> new Message(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("likes_count"),
                        rs.getString("picture_url")
                ));
    }




/*


    @Override
    public List<Message> findAll() {

        return jdbcTemplate.query(
                "select id, title, content, age, active from users",
                (rs, rowNum) -> new Message(
                        rs.getLong(  "id"),
                        rs.getString( "title"),
                        rs.getString( "content")
                ));


    }

 */
}
