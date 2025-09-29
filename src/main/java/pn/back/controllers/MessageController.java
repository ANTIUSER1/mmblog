package pn.back.controllers;


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
            @RequestBody Message message,
            @PathVariable("id") long id) {

        log.info("Request for  edit  message of ID  {}", id);
        Optional<Message> optionalMessage = messageService.modifyMessage(message, id);

        return getResponseEntity(id, optionalMessage, HttpStatus.EXPECTATION_FAILED);
    }


    @PostMapping("/api/posts/{id}/likes")
    public ResponseEntity incrementLike(@PathVariable("id") long id) {
        log.info("Request for increment likes for  message of ID  {}", id);
        Optional<Message> optionalMessage = messageService.incrementLikes(id);
        return getResponseEntity(id, optionalMessage, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/api/posts")
    public ResponseEntity addNewMessage(@RequestBody Message message) {
        log.info(" Request for adding message ");
        if (message == null) {
            log.info("Error --- Emty request");
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  mabe NULL ..."),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<Message> optionalMessage = messageService.addMessage(message);
        return ResponseEntity.ok(optionalMessage.get());
    }

    private ResponseEntity<?> getResponseEntity(long id, Optional<Message> optionalMessage, HttpStatus status) {
        if (optionalMessage.isPresent())
            return ResponseEntity.ok(optionalMessage.orElseThrow());
        return new ResponseEntity<>(new AppError(status.value(),
                "Request  with id " + id + " not not correct"),
                status);
    }
}
