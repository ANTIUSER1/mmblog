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
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return ResponseEntity.ok(commentRepository.getCommentsForMessage(id));
        } else
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Post with id " + id + " not found"),
                    HttpStatus.NOT_FOUND);
    }


    public ResponseEntity getCommentByNumberForPost(long id, int commentNumber) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        List<Comment> commentList = null;
        if (optionalMessage.isPresent()) {
            commentList = commentRepository.getCommentsForMessage(id);
        }
        if (commentList != null && commentNumber < commentList.size()) {
            Optional<Comment> optionalComment = Optional.of(commentList.get(commentNumber));
            return ResponseEntity.ok(optionalComment.get());
        } else
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Post with id " + id + " not found, or comment with  number " + commentNumber + "  does not exists "),
                    HttpStatus.NOT_FOUND);
    }

    public void addCommentsForPost(Comment comment, long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.getCommentList().add(comment);
            comment.setMessageKey(id);
            Optional<Comment> optionalComment = commentRepository.save(comment);
        }

    }

    public ResponseEntity editCommentsForPost(Comment commentNew, long id, int commentID) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            if (commentID < message.getCommentCount()) {
                Comment comment = message.getCommentList().get(commentID);
                comment.setContent(commentNew.getContent());
                return ResponseEntity.ok(commentRepository.save(comment));
            }
        }
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                "Post with id " + id + " not found"),
                HttpStatus.NOT_FOUND);
    }

}
