package pn.back.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pn.back.entities.Comment;
import pn.back.services.CommentService;


@RestController
@RequestMapping("/api")
@Slf4j
public class CommentsController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/posts/{id}/comments")
    public ResponseEntity getCommentsForPost(@PathVariable("id") long id) {
        return commentService.getCommentsForPost(id);
    }

    @GetMapping("/posts/{id}/comments/{commentNumber}")
    public ResponseEntity getCommenByNumberForPost(
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber
    ) {
        return commentService.getCommentByNumberForPost(id, commentNumber);
    }

    @PutMapping("/posts/{id}/comments/{commentID}")
    public ResponseEntity editCommentsForPost(
            @RequestBody Comment commentNew,
            @PathVariable("id") long id,
            @PathVariable("commentID") int commentID

    ) {
        return commentService.editCommentsForPost(commentNew, id, commentID);
    }

    @PostMapping("/posts/{id}/comments")
    public void addCommentsForPost(
            @RequestBody Comment comment,
            @PathVariable("id") long id) {
        commentService.addCommentsForPost(comment, id);
    }

}
