/**
 * сервис работы с комментариями
 */
package pn.back.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.repo.CommentRepository;
import pn.back.repo.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Optional<List<Comment>> getCommentsForPost(long id) {
        List<Comment> commentList = commentRepository.getCommentsForMessage(id);
        if (commentList != null && commentList.size() > 0) {
            return Optional.of(commentList);
        }
        return Optional.empty();
    }


    public Optional<Comment> getCommentByNumberForPost(long id, int commentNumber) {
        List<Comment> commentList = commentRepository.getCommentsForMessage(id);
        if (commentList.size() > 0 && commentNumber < commentList.size()) {
            return Optional.of(commentList.get(commentNumber));
        }
        return Optional.empty();
    }

    public Optional<Comment> addCommentsForPost(Comment comment, long id) {
        if (comment == null || comment.getContent().trim().isEmpty()) return Optional.empty();
        Message message = messageRepository.findById(id);
        if (message != null) {
            // Message message = optionalMessage.get();
            long commentsCount = 1 + message.getCommentsCount();
            message.setCommentsCount(commentsCount);
            return commentRepository.save(comment, message);
        }
        return Optional.empty();
    }

    public Optional<Comment> editCommentsForPost(Comment commentNew, long id, int commentNumber) {
        if (commentNew == null || commentNew.getContent().trim().isEmpty()) return Optional.empty();
        List<Comment> commentList = commentRepository.getCommentsForMessage(id);
        if (commentList.size() > 0 && commentNumber < commentList.size()) {
            Comment comment = commentList.get(commentNumber);
            comment.setContent(commentNew.getContent());
            return commentRepository.update(comment);
        }
        return Optional.empty();
    }

}
