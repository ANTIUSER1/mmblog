package pn.back.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pn.back.entities.Comment;
import pn.back.entities.Message;
import pn.back.errors.AppError;
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

    public ResponseEntity getCommentsForPost(long id) {


        List<Comment> commentList = commentRepository.getCommentsForMessage(id);
        if (commentList.size() > 0) {
            return ResponseEntity.ok(commentList);
        } else
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Post with id " + id + " not found, or no any comments of message with id " + id),
                    HttpStatus.NOT_FOUND);
    }


    public ResponseEntity getCommentByNumberForPost(long id, int commentNumber) {
        List<Comment> commentList = commentRepository.getCommentsForMessage(id);
        if (commentList.size() > 0 && commentNumber < commentList.size()) {
            return ResponseEntity.ok(commentList.get(commentNumber));
        } else
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Post with id " + id + " not found, or comment with  number " + commentNumber + "  does not exists "),
                    HttpStatus.NOT_FOUND);
    }

    public Optional<Comment> addCommentsForPost(Comment comment, long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            long commentsCount = 1 + message.getCommentsCount();
            message.setCommentsCount(commentsCount);
            return commentRepository.save(comment, message);
        }
        return Optional.empty();
    }

    public Optional<Comment> editCommentsForPost(Comment commentNew, long id, int commentNumber) {
        List<Comment> commentList = commentRepository.getCommentsForMessage(id);
        if (commentList.size() > 0 && commentNumber < commentList.size()) {

            System.out.println("CONMM NUM " + commentNumber);
            Comment comment = commentList.get(commentNumber);
            comment.setContent(commentNew.getContent());
            return commentRepository.update(comment);
        }
        return Optional.empty();
    }

}
