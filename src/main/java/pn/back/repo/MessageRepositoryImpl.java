package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.mappers.CommentMapper;
import pn.back.mappers.MessageMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class MessageRepositoryImpl implements MessageRepository {

    private static final String MAIN_SQL_SELECT = "  SELECT * FROM pract.blog.messages   ";
    private static final int ERROR_INT_RESULT = -1;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MessageMapper messageMapper;

    //
    @Override
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
        return jdbcTemplate.query(
                MAIN_SQL_SELECT + "  ORDER BY id ASC",
                messageMapper);
    }

    @Override
    public long numberOfRecords(String search) {
        return jdbcTemplate.query(
                MAIN_SQL_SELECT +
                        " WHERE title like '%" + search + "%'"
                        + " OR content like '%" + search + "%'"
                        + "  ORDER BY id ASC",
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
        try {
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
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> updateContent(long id, String content) {
        String sql = " UPDATE pract.blog.messages  " +
                "          SET  " +
                "                  content = '" + content + "'" +
                "     WHERE id =  " + id;
        log.info(
                "\n SQL UPDATE RUN \n{}", sql
        );
        try {

            int numberOfUpdates = jdbcTemplate.update(sql);
            if (numberOfUpdates == 0) log.info("No any updates");
            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return Optional.empty();
        }
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
        try {
            int numberOfUpdates = jdbcTemplate.update(sql);
            if (numberOfUpdates == 0) log.info("No any updates");
            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> incrementCommentsCount(Message message) {
        long commentsCount = message.getCommentsCount() + 1;
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  comments_count = " + commentsCount +
                "     WHERE id =  " + message.getId();
        log.info(
                "\n SQL UPDATE RUN \n{}", sql
        );
        try {
            int numberOfUpdates = jdbcTemplate.update(sql);
            if (numberOfUpdates == 0) log.info("No any updates");
            System.out.println(" NNNEEEWWW  MMESS " + findById(message.getId()).get());
            return findById(message.getId());
        } catch (Exception e) {
            log.info("no Message of found");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> incrementLikes(long id, long likes) {
        try {
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
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> save(Message message) {
        System.out.println("  \t MESSAGE \n" + message);
        List<String> tags = Arrays.asList(message.getTags()).stream().toList()
                .stream().map((t) -> "'" + t + "'").toList();
        System.out.println("STR-LIST : " + tags);
        String sql = " " +
                "INSERT INTO pract.blog.messages " +
                " (title, content, tags ) " +
                " VALUES ( '" + message.getTitle() + "'" +
                ", '" + message.getContent() + "' ," +
                " ARRAY[" + tags + "] )";
        System.out.println(
                "\n SQL UPDATE RUN \n" + sql
        );
        try {

            int numberOfUpdates = jdbcTemplate.update(sql);
            long lastId = jdbcTemplate.queryForObject(
                    "SELECT MAX(id) FROM  pract.blog.messages", Long.class
            );
            System.out.println("\n\n LAST ID " + lastId);

            return Optional.of(message);
        } catch (Exception e) {
            log.info("no Message  inputs");
            return Optional.empty();
        }
    }

    @Override
    public long delete(long id) {
        try {
            String sql = " " +
                    "DELETE FROM pract.blog.messages " +
                    " WHERE id = " + id;
            System.out.println(
                    "\n SQL UPDATE RUN \n" + sql
            );
            System.out.println("\n\n DELETE ID " + id);

            return jdbcTemplate.update(sql);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return ERROR_INT_RESULT;
        }
    }

    @Override
    public boolean addPicture(long id, String pictureUrl) {
        try {
            String sql = "    UPDATE pract.blog.messages  " +
                    "             SET  " +
                    "                  picture_url = '" + pictureUrl + "'" +
                    "     WHERE id =  " + id;
            log.info(
                    "\n SQL UPDATE RUN \n{}", sql
            );
            int numberOfUpdates = jdbcTemplate.update(sql);
            return numberOfUpdates == 1;
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return false;
        }
    }

    @Override
    public List<Comment> commentCount(Message message) {
        String sql =
                "SELECT COUNT(*) FROM pract.blog.comments   WHERE message_key = " + message.getId() + " ORDER BY id ASC";
        System.out.println("SQL ::: " + sql);
        try {
            return jdbcTemplate.query(sql, commentMapper);
        } catch (Exception e) {
            return List.of();
        }
    }


}
