package pn.back.repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Comment;


import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query(value = "     SELECT * FROM comments c WHERE c.message_id_key =:messageID ")
    List<Comment> getCommentsForMessage(long messageID);
}
