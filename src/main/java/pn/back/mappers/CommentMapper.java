/**
 * отображаем комментарий из базы в java
 */
package pn.back.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pn.back.entities.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CommentMapper implements RowMapper<Comment> {

    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getLong("message_key")
        );
    }
}
