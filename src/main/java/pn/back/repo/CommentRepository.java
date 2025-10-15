/**
 * контракт работы с комментариями
 */
package pn.back.repo;

import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.util.List;
import java.util.Optional;


public interface CommentRepository {

    Comment findById(long id);

    List<Comment> getCommentsForMessage(long messageID);

    Optional<Comment> save(Comment comment, Message message);

    Optional<Comment> update(Comment comment);


}
