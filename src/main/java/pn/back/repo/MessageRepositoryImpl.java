/**
 * механизм  работы с сообщениями
 */
package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.mappers.CommentMapper;
import pn.back.mappers.MessageMapper;
import pn.back.utils.ArrayUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class MessageRepositoryImpl implements MessageRepository {

    private static final String MAIN_SQL_SELECT = "  SELECT * FROM pract.blog.messages   ";
    private static final int ERROR_INT_RESULT = -1;

    @Autowired
    private Connection connection;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MessageMapper messageMapper;

    //
    @Override
    public Optional<Message> findById(long id) {
        String sql = MAIN_SQL_SELECT + " WHERE id = ? ";
        Message result =
                jdbcTemplate.queryForObject(sql,
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
        return jdbcTemplate.query(sql,
                messageMapper);
    }

    @Override
    public Optional<Message> updateContentTitle(long id, String content, String title) {
        try {
            String sql = "    UPDATE pract.blog.messages  " +
                    "             SET  " +
                    "                  content = ? , title =  ? " +
                    "     WHERE id = ? ";
            boolean updates = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, content);
                ps.setString(2, title);
                ps.setLong(3, id);
                return true;
            });

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
                "                  content = ?  " +
                "     WHERE id = ?";
        try {
            boolean updates =
                    jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                        ps.setString(1, content);
                        ps.setLong(2, id);
                        return true;
                    });

            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return Optional.empty();
        }
        //  return Optional.empty();
    }

    @Override
    public Optional<Message> updateTitle(long id, String title) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  title =  ?" +
                "     WHERE id = ? ";
        try {
            boolean updates = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, title);
                ps.setLong(2, id);
                return true;
            });

            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> incrementCommentsCount(Message message) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  comments_count = " + message.getCommentsCount() +
                "     WHERE id =  " + message.getId();
        try {
            int numberOfUpdates = jdbcTemplate.update(sql);
            if (numberOfUpdates == 0) log.info("No any updates");
            System.out.println(" NNNEEEWWW  MMESS "
                    + findById(message.getId()).get() + " \n\n\n");
            return Optional.of(message);
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
                    "                  likes_count = ? " +
                    "     WHERE id =  ? ";
            boolean updates = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setLong(1, likes);
                ps.setLong(2, id);
                return true;
            });
            if (updates) log.info("No any updates");
            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> save(Message message) {

        String sql = null;
        boolean updates = false;
        System.out.println(message);
        List<String> tags = null;

        if (message.getTags() != null) {
            return saveWithTags(message);
        } else {
            return saveTagsFree(message);
        }


















        /*
        if (message.getTags() != null) {
            tags = Arrays.asList(message.getTags()).stream().toList()
                    .stream().map((t) -> "'" + t + "'").toList();

        }

        try {
            if (tags != null) {
                String tStr = tags.toString();
                sql =
                        "INSERT INTO pract.blog.messages " +
                                " (title, content, tags ) " +
                                " VALUES (  ? ,? ,  ARRAY[ ? ] )";
                updates = jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
                    @Override
                    public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                        ps.setString(1, message.getTitle());
                        ps.setString(2, message.getContent());
                        ps.setString(3, tStr);
                        return true;
                    }
                });
                System.out.println("DONE----TAGS------- ::: " + updates);
            } else {
                sql =
                        "INSERT INTO pract.blog.messages " +
                                " (title, content) " +
                                " VALUES ( ? ,  ?  )";
                updates = jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
                    @Override
                    public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                        System.out.println("GG   INNER ");
                        ps.setString(1, message.getTitle());
                        ps.setString(2, message.getContent());

                        return true;
                    }
                });
            }
            System.out.println("DONE ::: " + updates);
            if (updates) {

                long lastId = jdbcTemplate.queryForObject(
                        "SELECT MAX(id) FROM  pract.blog.messages", Long.class);
                System.out.println("\n\n LAST ID " + lastId);
                return findById(lastId);
            } else
                return Optional.empty();

        } catch (Exception e) {
            log.info("no Message  inputs");
            return Optional.empty();
        }

         */
        //return null;
    }

    private Optional<Message> saveTagsFree(Message message) {
        String sql =
                "INSERT INTO pract.blog.messages " +
                        " (title, content  ) " +
                        " VALUES (  ? ,  ?  ) ";
        PreparedStatement prs = null;
        try {
            prs = connection.prepareStatement(sql);
            prs.setString(1, message.getTitle());
            prs.setString(2, message.getContent());
            prs.execute();
        } catch (SQLException e) {
            System.out.println("\n ERROR ");
            throw new RuntimeException(e);
        }

        long lastId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM  pract.blog.messages", Long.class);
        System.out.println("\n\n LAST ID " + lastId);
        return findById(lastId);
    }

    private Optional<Message> saveWithTags(Message message) {
        boolean inserted = false;
        String sql =
                "INSERT INTO pract.blog.messages " +
                        " (title, content, tags ) " +
                        " VALUES (  ? ,? , ARRAY [ ? ])";
        String tgs = ArrayUtils.convertFromArray(message.getTags());
        System.out.println(tgs);

        PreparedStatement prs = null;
        try {
            prs = connection.prepareStatement(sql);
            prs.setString(1, message.getTitle());
            prs.setString(2, message.getContent());
            prs.setString(3, tgs);
            inserted = prs.execute();
        } catch (SQLException e) {
            System.out.println("\n ERROR ");
            throw new RuntimeException(e);
        }
        long lastId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM  pract.blog.messages", Long.class);
        System.out.println("\n\n LAST ID " + lastId);
        //  message.setId(lastId);

        System.out.println("NEW MSG: \n" + findById(lastId));
        if (inserted) return Optional.empty();
        else return Optional.empty();
    }

    @Override
    public long delete(long id) {
        try {
            String sql =
                    "DELETE FROM pract.blog.messages " +
                            " WHERE id = " + id;
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
    public List<Comment> commentsForMessage(Message message) {
        String sql =
                "SELECT COUNT(*) FROM pract.blog.comments   WHERE message_key = " + message.getId() + " ORDER BY id ASC";
        try {
            return jdbcTemplate.query(sql, commentMapper);
        } catch (Exception e) {
            return List.of();
        }
    }


}
