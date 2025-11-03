/**
 * механизм  работы с сообщениями
 */
package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
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

import static pn.back.services.MessageService.MAX_COMMENTS_SIZE;
import static pn.back.services.MessageService.MAX_TITLE_SIZE;

@Repository
@Transactional
@Slf4j
public class MessageRepositoryImpl implements MessageRepository {

    public static final String MAIN_SQL_SELECT = "  SELECT * FROM   messages ";
    public static final String MAIN_SQL_TEST_SELECT = "  SELECT * FROM pract.blog_TEST.messages   ";
    private static final int ERROR_INT_RESULT = -1;

    private final Connection connection;


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MessageMapper messageMapper;

    public MessageRepositoryImpl(JdbcTemplate jdbcTemplate) throws SQLException {
        this.jdbcTemplate = jdbcTemplate;
        this.connection = jdbcTemplate.getDataSource().getConnection();
    }

    @Override
    public Message findById(long id) {
        String sql = MAIN_SQL_SELECT + " WHERE id =? ";
        List<Message> resultList = null;
        try {
            resultList =
                    jdbcTemplate.query(sql,
                            new PreparedStatementSetter() {
                                @Override
                                public void setValues(PreparedStatement ps) throws SQLException {
                                    ps.setLong(1, id);
                                }
                            },
                            messageMapper);
        } catch (Exception e) {
            log.info("\n   Message with ID " + id + " not exists ");

        }
        if (resultList != null && resultList.size() == 1) return resultList.get(0);
        return null;
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
                        " WHERE title LIKE  ?  " +
                        "     OR  content LIKE ?   ORDER BY id ASC",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, "%" + search + "%");
                        ps.setString(2, "%" + search + "%");
                    }
                },
                messageMapper).size();
    }

    @Override
    public List<Message> showAllByPage(int page, int limit, String search) {
        String sql = MAIN_SQL_SELECT
                + " WHERE "
                + "title LIKE  ? "
                + "   OR content LIKE ? "
                + "  ORDER BY id asc  "
                + "  LIMIT  ?  "
                + " OFFSET  ? ";
        return jdbcTemplate.query(sql,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, "%" + search + "%");
                        ps.setString(2, "%" + search + "%");
                        ps.setInt(3, limit);
                        ps.setInt(4, page);
                    }
                }, messageMapper
        );

    }

    @Override
    public Message updateContentTitle(long id, String content, String title) {
        if (
                title.trim().length() < MAX_TITLE_SIZE ||
                        content.trim().length() > MAX_COMMENTS_SIZE
        ) {
            log.info("Error: TITLE LENGTH : {}; COMTENT LENGTH: {}", title.trim().length(), content.trim().length());
            return null;
        }
        try {
            String sql = "    UPDATE           messages  " +
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
        if (
                content.trim().length() > MAX_COMMENTS_SIZE
        ) {
            content = content.substring(0, MAX_COMMENTS_SIZE);
        }
        String sql = " UPDATE           messages  " +
                "          SET  " +
                "                  content = ?  " +
                "     WHERE id = ?";
        try {
            String finalContent = content;
            boolean updates =
                    jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                        ps.setString(1, finalContent);
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
        if (
                title.trim().length() < MAX_TITLE_SIZE
        ) {
            title = title.substring(0, MAX_TITLE_SIZE);
        }
        String sql = "    UPDATE           messages  " +
                "             SET  " +
                "                  title =  ?" +
                "     WHERE id = ? ";
        try {
            String finalTitle = title;
            boolean updates = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, finalTitle);
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
        String sql = "    UPDATE           messages  " +
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
            String sql = "    UPDATE           messages  " +
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
                "INSERT INTO           messages " +
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
                "SELECT MAX(id) FROM            messages", Long.class);
        return findById(lastId);
    }

    private Message saveWithTags(Message message) {
        boolean inserted = false;
        String sql =
                "INSERT INTO           messages " +
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
                "SELECT MAX(id) FROM            messages", Long.class);
        if (!inserted) return findById(lastId);
        else return null;
    }

    @Override
    public long delete(long id) {
        try {
            String sql =
                    "DELETE FROM           messages " +
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
            String sql = "    UPDATE           messages  " +
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
                "SELECT COUNT(*) FROM           comments   WHERE message_key = " + message.getId() + " ORDER BY id ASC";
        try {
            return jdbcTemplate.query(sql, commentMapper);
        } catch (Exception e) {
            return List.of();
        }
    }


}
