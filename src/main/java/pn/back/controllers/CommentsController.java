package pn.back.controllers;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pn.back.entities.Comment;
import pn.back.services.CommentService;


@RestController
//@RequestMapping("/api")
@Slf4j
public class CommentsController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/api/posts/{id}/comments")
    public ResponseEntity getCommentsForPost(@PathVariable("id") long id) {
        return commentService.getCommentsForPost(id);
    }

    @GetMapping("/api/posts/{id}/comments/{commentNumber}")
    public ResponseEntity getCommenByNumberForPost(
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber
    ) {
        return commentService.getCommentByNumberForPost(id, commentNumber);
    }

    @PutMapping("/api/posts/{id}/comments/{commentNumber}")
    public ResponseEntity editCommentsForPost(
            @Nullable @RequestBody Comment commentNew,
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber

    ) {
        log.info("\n  Edit comment № {} of {} msg ", commentNumber, id);
        return commentService.editCommentsForPost(commentNew, id, commentNumber);

    }

    @PostMapping("/api/posts/{id}/comments")
    public void addCommentsForPost(
            @RequestBody Comment comment,
            @PathVariable("id") long id) {
        commentService.addCommentsForPost(comment, id);
    }

}
