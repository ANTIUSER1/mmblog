/**
 * отображаем сообщение из базы в java
 */
package pn.back.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pn.back.entities.Message;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Message(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getInt("likes_count"),
                rs.getInt("comments_count"),
                rs.getString("picture_url"),
                rs.getArray("tags")
        );
    }
}
