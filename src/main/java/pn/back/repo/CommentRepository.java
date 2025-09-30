package pn.back.repo;

import pn.back.entities.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository {

    //    @Query(
//            """
//                    SELECT * FROM comments c WHERE c.message_id_key =:messageID
//                    """
//    )
    List<Comment> getCommentsForMessage(long messageID);

    Optional<Comment> save(Comment comment);
}
