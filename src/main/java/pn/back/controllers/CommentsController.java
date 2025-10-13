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
import pn.back.utils.ControllerUtil;

import java.util.List;
import java.util.Optional;

import static pn.back.utils.ControllerUtil.getResponseEntity;


@RestController
@Slf4j
public class CommentsController {

    @Autowired
    private CommentService commentService;


    @GetMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments")
    public ResponseEntity getCommentsForPost(@PathVariable("id") long id) {
        log.info("Request for all comments of post with ID {}", id);
        Optional<List<Comment>> result = commentService.getCommentsForPost(id);
        return getResponseEntity(id, result, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments/{commentNumber}")
    public ResponseEntity getCommenByNumberForPost(
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber
    ) {
        log.info("Request for   comment #{}  of post with ID {}", commentNumber, id);
        Optional<Comment> result = commentService.getCommentByNumberForPost(id, commentNumber);
        return getResponseEntity(id, result, HttpStatus.BAD_REQUEST);
    }

    @PutMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments/{commentNumber}")
    public ResponseEntity editCommentsForPost(
            @Nullable @RequestBody Comment commentNew,
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber

    ) {
        log.info("\n  Edit comment № {} of {} msg ", commentNumber, id);
        Optional<Comment> result = commentService.editCommentsForPost(commentNew, id, commentNumber);
        return getResponseEntity(id, result, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments")
    public void addCommentsForPost(
            @RequestBody Comment comment,
            @PathVariable("id") long id) {

        log.info("\n  Request for adding  comment  for  {} msg ", id);
        commentService.addCommentsForPost(comment, id);
    }
}
