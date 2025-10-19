/**
 * контрлллер работы с комментариями
 */
package pn.back.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import pn.back.entities.Comment;
import pn.back.errors.AppError;
import pn.back.services.CommentService;
import pn.back.utils.ControllerUtil;

import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/hello")
    @ResponseBody
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello, World!");
    }


    @GetMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments")
    @ResponseBody
    public ResponseEntity getCommentsForPost(@NonNull @PathVariable("id") long id) {
        log.info("Request for all comments of post with ID {}", id);
        Optional<List<Comment>> result = commentService.getCommentsForPost(id);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());
        else
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ... or no such message.... or such  comment "),
                    HttpStatus.BAD_REQUEST);
    }

    @GetMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments/{commentNumber}")
    @ResponseBody
    public ResponseEntity getCommenByNumberForPost(
            @NonNull @PathVariable("id") long id,
            @NonNull @PathVariable("commentNumber") int commentNumber
    ) {
        log.info("Request for   comment #{}  of post with ID {}", commentNumber, id);
        Optional<Comment> result = commentService.getCommentByNumberForPost(id, commentNumber);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());
        else
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ... or no such message.... or such  comment "),
                    HttpStatus.BAD_REQUEST);
    }

    @PutMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments/{commentNumber}")
    @ResponseBody
    public ResponseEntity editCommentsForPost(
            @NonNull @RequestBody Comment commentNew,
            @PathVariable("id") long id,
            @PathVariable("commentNumber") int commentNumber

    ) {
        log.info("\n  Edit comment № {} of {} msg ", commentNumber, id);
        Optional<Comment> result = commentService.editCommentsForPost(commentNew, id, commentNumber);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());
        else
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ... or no such message ... or empty comment "),
                    HttpStatus.BAD_REQUEST);
    }

    @PostMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments")
    @ResponseBody
    public ResponseEntity addCommentsForPost(
            @NonNull @RequestBody Comment comment,
            @PathVariable("id") long id) {

        log.info("\n  Request for adding  comment  for  {} msg ", id);
        Optional<Comment> commentOptional = commentService.addCommentsForPost(comment, id);
        if (commentOptional.isPresent())
            return ResponseEntity.ok(commentOptional.get());
        else
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ... or no such message? or empty comment "),
                    HttpStatus.BAD_REQUEST);
    }
}
