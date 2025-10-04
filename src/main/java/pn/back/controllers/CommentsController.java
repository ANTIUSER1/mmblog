/**
 * контрлллер работы с комментариями
 */
package pn.back.controllers;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pn.back.entities.Comment;
import pn.back.services.CommentService;

import java.util.List;
import java.util.Optional;

import static pn.back.utils.ControllerUtil.getResponseEntity;


@RestController
//@RequestMapping("/api")
@Slf4j
public class CommentsController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/api/posts/{id}/comments")
    public ResponseEntity getCommentsForPost(@PathVariable("id") long id) {
        Optional<List<Comment>> result = commentService.getCommentsForPost(id);
        return getResponseEntity(id, result, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/posts/{id}/comments/{commentNumber}")
    public ResponseEntity getCommenByNumberForPost(
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber
    ) {
        Optional<Comment> result = commentService.getCommentByNumberForPost(id, commentNumber);
        return getResponseEntity(id, result, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/posts/{id}/comments/{commentNumber}")
    public ResponseEntity editCommentsForPost(
            @Nullable @RequestBody Comment commentNew,
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber

    ) {
        log.info("\n  Edit comment № {} of {} msg ", commentNumber, id);
        Optional<Comment> result = commentService.editCommentsForPost(commentNew, id, commentNumber);
        return getResponseEntity(id, result, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/posts/{id}/comments")
    public void addCommentsForPost(
            @RequestBody Comment comment,
            @PathVariable("id") long id) {
        commentService.addCommentsForPost(comment, id);
    }
}
