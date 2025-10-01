package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.mappers.CommentMapper;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class CommentRepositoryImpl implements CommentRepository {

    private static final String MAIN_SQL_SELECT = "  SELECT * FROM pract.blog.comments   ";

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Optional<Comment> findById(long id) {
        String sql = MAIN_SQL_SELECT + " WHERE id = " + id;
        Comment result = jdbcTemplate.queryForObject(sql, commentMapper);
        if (result == null) return Optional.empty();
        return Optional.of(result);
    }

    @Override
    public List<Comment> getCommentsForMessage(long messageID) {
        String sql = MAIN_SQL_SELECT +
                "  WHERE message_key = " + messageID + " ORDER BY id ASC";
        return jdbcTemplate.query(sql, commentMapper);
    }

    @Override
    public Optional<Comment> save(Comment comment, Message message) {
        String sql = " INSERT INTO pract.blog.comments " +
                " ( content , message_key )" +
                " VALUES  ( '" + comment.getContent() + "' , " +
                message.getId() +
                "  )";
        messageRepository.incrementCommentsCount(message);
        try {
            int numberOfUpdates = jdbcTemplate.update(sql);
            long lastId = jdbcTemplate.queryForObject(
                    "SELECT MAX(id) FROM  pract.blog.comments", Long.class
            );
            return findById(lastId);
        } catch (Exception e) {
            log.info("no Message  inputs");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Comment> update(Comment comment) {
        String sql = "UPDATE pract.blog.comments " +
                "   SET  content = '" + comment.getContent() + "'  " +
                " WHERE id = " + comment.getId();
        try {
            int numberOfUpdates = jdbcTemplate.update(sql);
            if (numberOfUpdates == 0) log.info("No any updates");
            return findById(comment.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
