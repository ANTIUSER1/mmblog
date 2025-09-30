package pn.back.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Comment;
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
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Comment> getCommentsForMessage(long messageID) {
        String sql = MAIN_SQL_SELECT + "  WHERE message_key = " + messageID;
        System.out.println("SQL ::: " + sql);
        return jdbcTemplate.query(sql, commentMapper);
    }

    @Override
    public Optional<Comment> save(Comment comment) {
        return Optional.empty();
    }
}
