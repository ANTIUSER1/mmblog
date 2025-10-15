/**
 * механизм  работы с сообщениями
 */
package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.mappers.CommentMapper;
import pn.back.mappers.MessageMapper;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
    public Message findById(long id) {
        String sql = MAIN_SQL_SELECT + " WHERE id =  " + id;
        Message result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, messageMapper
            );
        } catch (Exception e) {
            log.info("\n   Message with ID " + id + " not exists ");

        }
        return result;
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
    public Message updateContentTitle(long id, String content, String title) {
        try {
            String sql = "    UPDATE pract.blog.messages  " +
                    "             SET  " +
                    "                  content = ? , title =  ? " +
                    "     WHERE id = ? ";
            boolean updates = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, content);
                ps.setString(2, title);
                ps.setLong(3, id);
                ps.execute();
                return true;
            });

            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return null;
        }
    }

    @Override
    public Message updateContent(long id, String content) {
        String sql = " UPDATE pract.blog.messages  " +
                "          SET  " +
                "                  content = ?  " +
                "     WHERE id = ?";
        try {
            boolean updates =
                    jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                        ps.setString(1, content);
                        ps.setLong(2, id);
                        ps.execute();
                        return true;
                    });

            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return null;
        }
        //  return Optional.empty();
    }

    @Override
    public Message updateTitle(long id, String title) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  title =  ?" +
                "     WHERE id = ? ";
        try {
            boolean updates = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, title);
                ps.setLong(2, id);
                ps.execute();
                return true;
            });
            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return null;
        }
    }

    @Override
    public Message incrementCommentsCount(Message message) {
        String sql = "    UPDATE pract.blog.messages  " +
                "             SET  " +
                "                  comments_count = ? " +
                "     WHERE id = ? ";
        try {
            boolean updates = jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setLong(1, message.getCommentsCount());
                    ps.setLong(2, message.getId());
                    ps.execute();
                    return true;
                }
            });
            return findById(message.getId());
        } catch (Exception e) {
            log.info("no Message of found");
            return null;
        }
    }

    @Override
    public Message incrementLikes(long id, long likes) {
        try {
            String sql = "    UPDATE pract.blog.messages  " +
                    "             SET  " +
                    "                  likes_count = ? " +
                    "     WHERE id =  ? ";
            boolean updates = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setLong(1, likes);
                ps.setLong(2, id);
                ps.execute();
                return true;
            });
            if (updates) log.info("No any updates");
            return findById(id);
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return null;
        }
    }

    @Override
    public Message save(Message message) {

        String sql = null;
        boolean updates = false;
        System.out.println(message);
        List<String> tags = null;

        if (message.getTags() != null) {
            return saveWithTags(message);
        } else {
            return saveTagsFree(message);
        }
    }

    private Message saveTagsFree(Message message) {
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
            log.info(" ERROR {}", e.getMessage());
        }

        long lastId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM  pract.blog.messages", Long.class);
        System.out.println("\n\n LAST ID " + lastId);
        return findById(lastId);
    }

    private Message saveWithTags(Message message) {
        boolean inserted = false;
        String sql =
                "INSERT INTO pract.blog.messages " +
                        " (title, content, tags ) " +
                        " VALUES (  ? , ? , ? )";
        PreparedStatement prs = null;
        try {
            prs = connection.prepareStatement(sql);
            Array sqlArray = connection.createArrayOf("TEXT", message.getTags());
            prs.setString(1, message.getTitle());
            prs.setString(2, message.getContent());
            prs.setArray(3, sqlArray);
            System.out.println("\nPRS:\n " + prs);
            prs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n ERROR " + e.getMessage());
            //   throw new RuntimeException(e);
        }
        long lastId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM  pract.blog.messages", Long.class);
        if (!inserted) return findById(lastId);
        else return null;
    }

    @Override
    public long delete(long id) {
        try {
            String sql =
                    "DELETE FROM pract.blog.messages " +
                            " WHERE id = ?";
            PreparedStatement prs = connection.prepareStatement(sql);
            prs.setLong(1, id);
            return prs.executeUpdate();
        } catch (Exception e) {
            log.info("no Message of id {}", id);
            return ERROR_INT_RESULT;
        }
    }

    @Override
    public boolean addPicture(long id, String pictureUrl) {
        if (findById(id) == null) {
            log.info("\n No such message with ID {}", id);
            return false;
        }
        try {
            String sql = "    UPDATE pract.blog.messages  " +
                    "             SET  " +
                    "                  picture_url = ?  " +
                    "     WHERE id = ?  ";
            int updates = jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
                @Override
                public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, pictureUrl);
                    ps.setLong(2, id);
                    return ps.executeUpdate();
                }
            });
            return updates == 1;
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
