/**
 * контрлллер работы с сообщениями
 */
package pn.back.controllers;


import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pn.back.entities.Message;
import pn.back.entities.MessagePageData;
import pn.back.errors.AppError;
import pn.back.services.MessageService;
import pn.back.utils.ControllerUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static pn.back.utils.ControllerUtil.getResponseEntity;

@RestController
@Slf4j
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/hello0")
    @ResponseBody
    public ResponseEntity<String> sayHello() {
        System.out.println("OOO-000");
        return ResponseEntity.ok("Hello, World-0!");
    }

    @GetMapping(ControllerUtil.ALL_POSTS_API + "/{id}")
    @ResponseBody
    public Message showById(@Validated @PathVariable("id") long id) {
        log.info("Request for   messages  with ID {}", id);
        return messageService.findById(id);
    }

    @GetMapping(ControllerUtil.ALL_POSTS_API + "/all")
    @ResponseBody
    public List<Message> showAll() {
        log.info("Request for all messages");
        return messageService.findAll();
    }

    @GetMapping(ControllerUtil.ALL_POSTS_API)
    @ResponseBody
    public MessagePageData seshowAllPG(
            @Validated @NonNull @RequestParam(value = "pageNumber", defaultValue = "0") int page,
            @NonNull @RequestParam(value = "pageSize", defaultValue = "2") int pageSize,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {

        if (search != null) search = search.trim();
        if (pageSize < 1) return null;
        log.info(" \n\n-------\nRequest for showing   messages by criteria title or content has string {} page {} line to {} line",
                search, page, page + pageSize
        );

        return messageService.showAllPG(page, pageSize, search);
    }

    @PutMapping(ControllerUtil.ALL_POSTS_API + "/{id}")
    @ResponseBody
    public ResponseEntity edit(
            @Validated @Nullable @RequestBody Message message,
            @PathVariable("id") long id) throws SQLException {
        if (message != null) {
            log.info("\nRequest for  edit  message of ID  {}", id);
            Optional<Message> optionalMessage = Optional.of(messageService.modifyMessage(message, id));
            return getResponseEntity(id, optionalMessage, HttpStatus.EXPECTATION_FAILED);
        } else {
            log.info("Error --- Emty request");
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ..."),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(ControllerUtil.ALL_POSTS_API + "/{id}/likes")
    @ResponseBody
    public ResponseEntity incrementLikes(@Validated @NonNull @PathVariable("id") long id) {
        log.info("Request for increment likes for  message of ID  {}", id);
        Optional<Message> optionalMessage = messageService.incrementLikes(id);
        return getResponseEntity(id, optionalMessage, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(ControllerUtil.ALL_POSTS_API + "/{id}/comments-count")
    @ResponseBody
    public ResponseEntity incrementComments(@Validated @NonNull @PathVariable("id") long id) {
        log.info("Request for increment likes for  message of ID  {}", id);
        Optional<Message> optionalMessage = messageService.incrementComments(id);
        return getResponseEntity(id, optionalMessage, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(ControllerUtil.ALL_POSTS_API)
    @ResponseBody
    public ResponseEntity addNewMessage(
            @Validated @Nullable @RequestBody Message message) {
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

    @DeleteMapping(ControllerUtil.ALL_POSTS_API + "/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePost(
            @Validated @NonNull @PathVariable("id") long id) {
        log.info(" Request for deleting  message of {} ", id);
        long deleted = messageService.delete(id);
        if (deleted == 1L)
            return ResponseEntity.ok("Deleted " + deleted + " record");
        return getResponseEntity(id, Optional.empty(), HttpStatus.BAD_REQUEST);
    }
}
