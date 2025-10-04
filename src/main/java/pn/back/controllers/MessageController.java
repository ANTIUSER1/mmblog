/**
 * контрлллер работы с сообщениями
 */
package pn.back.controllers;


import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pn.back.entities.Message;
import pn.back.entities.MessagePageData;
import pn.back.errors.AppError;
import pn.back.services.MessageService;

import java.util.List;
import java.util.Optional;

import static pn.back.utils.ControllerUtil.getResponseEntity;

@RestController
//@RequestMapping("/")
@Slf4j
public class MessageController {

    @Autowired
    private MessageService messageService;


    @GetMapping(value = "/api/posts/all")
    public List<Message> showAll() {
        log.info("Request for all messages");


        return messageService.findAll();
    }

    @GetMapping("/api/posts")
    public MessagePageData seshowAllPG(
            @RequestParam(value = "pageNumber", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "1") int pageSize,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {

        if (search != null) search = search.trim();
        if (pageSize < 1) return null;
        log.info(" \n\n-------\nRequest for showing   messages by criteria title or content has string {} page {} line to {} line",
                search, page, page + pageSize
        );

        return messageService.showAllPG(page, pageSize, search);
    }

    @PutMapping("/api/posts/{id}")
    public ResponseEntity edit(
            @Nullable @RequestBody Message message,
            @PathVariable("id") long id) {
        if (message != null) {
            log.info("Request for  edit  message of ID  {}", id);

            Optional<Message> optionalMessage = messageService.modifyMessage(message, id);

            return getResponseEntity(id, optionalMessage, HttpStatus.EXPECTATION_FAILED);
        } else {
            log.info("Error --- Emty request");
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ..."),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/posts/{id}/likes")
    public ResponseEntity incrementCC(@PathVariable("id") long id) {
        log.info("Request for increment likes for  message of ID  {}", id);
        Optional<Message> optionalMessage = messageService.incrementLikes(id);
        return getResponseEntity(id, optionalMessage, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/posts/{id}/comments-count")
    public ResponseEntity incrementLike(@PathVariable("id") long id) {
        log.info("Request for increment likes for  message of ID  {}", id);
        Optional<Message> optionalMessage = messageService.incrementComments(id);
        return getResponseEntity(id, optionalMessage, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/posts")
    public ResponseEntity addNewMessage(@Nullable @RequestBody Message message) {
        log.info(" Request for adding message ");
        if (message != null) {
            Optional<Message> optionalMessage = messageService.addMessage(message);
            return ResponseEntity.ok(optionalMessage.get());
        } else {
            log.info("Error --- Emty request");
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ..."),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") long id) {
        log.info(" Request for deleting  message of {} ", id);
        long deleted = messageService.delete(id);
        if (deleted == 1L)
            return ResponseEntity.ok("Deleted " + deleted + " record");
        return getResponseEntity(id, Optional.empty(), HttpStatus.BAD_REQUEST);
    }
}
