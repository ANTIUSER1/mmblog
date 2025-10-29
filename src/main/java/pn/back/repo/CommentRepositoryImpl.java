/**
 * механизм работы с комментариями
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class CommentRepositoryImpl implements CommentRepository {

    private static final String MAIN_SQL_SELECT = "  SELECT * FROM  comments   ";
//    private static final String MAIN_SQL_SELECT = "  SELECT * FROM            comments   ";

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Comment findById(long id) {
        String sql = MAIN_SQL_SELECT + " WHERE id = ?  ";
        List<Comment> resultList = null;

        try {
            resultList = jdbcTemplate.query(sql,
                    new PreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setLong(1, id);
                        }
                    },
                    commentMapper);
        } catch (Exception e) {
            log.info("No comment of ID " + id);
        }
        if (resultList != null && resultList.size() == 1) return resultList.get(0);
        return null;
    }

    @Override
    public List<Comment> getCommentsForMessage(long messageID) {
        String sql = MAIN_SQL_SELECT +
                "  WHERE message_key = " + messageID + " ORDER BY id ASC";
        return jdbcTemplate.query(sql, commentMapper);
    }

    @Override
    public Optional<Comment> save(Comment comment, Message message) {
        String sql = " INSERT INTO            comments " +
                " ( content , message_key )" +
                " VALUES  ( ? , ?   )";
        messageRepository.incrementCommentsCount(message);
        try {
            int numberOfUpdates = jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
                @Override
                public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, comment.getContent());
                    ps.setLong(2, message.getId());
                    return ps.executeUpdate();
                }
            });
            long lastId = jdbcTemplate.queryForObject(
                    "SELECT MAX(id) FROM             comments", Long.class
            );
            return Optional.of(findById(lastId));
        } catch (Exception e) {
            log.info("no Message  inputs");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Comment> update(Comment comment) {
        String sql = "UPDATE            comments " +
                " SET  content =  ?  " +
                " WHERE id = ?";
        try {
            int updates = jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
                @Override
                public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, comment.getContent());
                    ps.setLong(2, comment.getId());
                    return ps.executeUpdate();
                }
            });
            if (updates == 0) log.info("No any updates");
            return Optional.of(findById(comment.getId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
