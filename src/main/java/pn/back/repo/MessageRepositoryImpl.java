package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Message;
import pn.back.mappers.MessageMapper;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class MessageRepositoryImpl implements MessageRepository {

    private static final String MAIN_SQL_SELECT = "  SELECT * FROM pract.blog.messages   ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MessageMapper messageMapper;

    //
    //  @Override
    public Optional<Message> findById(long id) {
        String sql = MAIN_SQL_SELECT + " WHERE id = " + id;
        Message result = jdbcTemplate.queryForObject(sql,
                messageMapper
        );
        if (result == null) return Optional.empty();
        return Optional.of(result);
    }

    @Override
    public List<Message> findAll() {
        Optional<Message> m = findById(1);
        return jdbcTemplate.query(
                MAIN_SQL_SELECT + "  ORDER BY id asc",
                messageMapper);
    }

    @Override
    public long numberOfRecords(String search) {
        return jdbcTemplate.query(
                MAIN_SQL_SELECT +
                        " WHERE title like '%" + search + "%'"
                        + " OR content like '%" + search + "%'"
                        + "  ORDER BY id asc",
                messageMapper).size();

    }

    @Override
    public List<Message> showAllByPage(int page, int limit, String search) {
        String sql = MAIN_SQL_SELECT +
                " WHERE title LIKE '%" + search + "%'"
                + "   OR content LIKE '%" + search + "%' "
                + "  ORDER BY id asc" +
                " OFFSET " + page + "  LIMIT " + limit;
        System.out.println(
                "\n SQL SELECT RUN  \n " + sql
        );
        return jdbcTemplate.query(sql,
                messageMapper);
    }

    @Override
    public Optional<Message> updateContentTitle(long id, String content, String title) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  content = '" + content + "', " +
                "                  title = '" + title + "'" +
                "     WHERE id =  " + id;
        log.info(
                "\n SQL UPDATE RUN \n{}", sql
        );
        int numberOfUpdates = jdbcTemplate.update(sql);
        if (numberOfUpdates == 0) log.info("No any updates");
        return findById(id);
    }

    @Override
    public Optional<Message> updateContent(long id, String content) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  content = '" + content + "'" +
                "     WHERE id =  " + id;
        log.info(
                "\n SQL UPDATE RUN \n{}", sql
        );
        int numberOfUpdates = jdbcTemplate.update(sql);
        if (numberOfUpdates == 0) log.info("No any updates");
        return findById(id);
    }

    @Override
    public Optional<Message> updateTitle(long id, String title) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  title = '" + title + "'" +
                "     WHERE id =  " + id;
        log.info(
                "\n SQL UPDATE RUN \n{}", sql
        );
        int numberOfUpdates = jdbcTemplate.update(sql);
        if (numberOfUpdates == 0) log.info("No any updates");
        return findById(id);
    }

    @Override
    public Optional<Message> incrementLikes(long id, long likes) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  likes_count = '" + likes + "'" +
                "     WHERE id =  " + id;
        log.info(
                "\n SQL UPDATE RUN \n{}", sql
        );
        int numberOfUpdates = jdbcTemplate.update(sql);
        if (numberOfUpdates == 0) log.info("No any updates");
        return findById(id);
    }

    @Override
    public Optional<Message> save(Message message) {
        System.out.println("\n---\n" + message + "\n**************************\n");

        String sql = " " +
                "INSERT INTO pract.blog.messages " +
                " (title, content) " +
                " VALUES ( '" + message.getTitle() + "'" +
                ", '" + message.getContent() + "' )";
        System.out.println(
                "\n SQL UPDATE RUN \n" + sql
        );
        int numberOfUpdates = jdbcTemplate.update(sql);
        long lastId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM  pract.blog.messages", Long.class
        );
        System.out.println("\n\n LAST ID " + lastId);

        return findById(lastId);
    }

    @Override
    public long delete(long id) {
        String sql = " " +
                "DELETE FROM pract.blog.messages " +
                " WHERE id = " + id;
        System.out.println(
                "\n SQL UPDATE RUN \n" + sql
        );
        System.out.println("\n\n DELETE ID " + id);
        return jdbcTemplate.update(sql);
    }


}
