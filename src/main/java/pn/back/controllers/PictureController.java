/**
 * контрлллер работы с картинками
 */
package pn.back.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pn.back.entities.Message;
import pn.back.errors.AppError;
import pn.back.services.MessageService;
import pn.back.utils.ControllerUtil;

import java.io.IOException;
import java.util.Optional;

import static pn.back.utils.ControllerUtil.getResponseEntity;

@RestController
@Slf4j
public class PictureController {


    @Autowired
    private String uploadDir;


    @Autowired
    private MessageService messageService;

    @PutMapping(ControllerUtil.ALL_POSTS_API + "/{id}/image")
    @ResponseBody
    public ResponseEntity<?> addPicture(
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") long id
    ) throws IOException {
        if (file != null && file.getBytes().length > 0) {
            log.info("Request for Updating picture for message {} ", id);
            Optional<Message> message = messageService.addPicture(file, id);
            if (message.isPresent()) return ResponseEntity.ok(message.get());
            else return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ... or no such message"),
                    HttpStatus.BAD_REQUEST);
        }
        return getResponseEntity(id, Optional.empty(), HttpStatus.BAD_REQUEST, " or File not defined");
    }

    @GetMapping(ControllerUtil.ALL_POSTS_API + "/{id}/image")
    @ResponseBody
    public ResponseEntity<?> getPicture(
            @Validated @NonNull @PathVariable("id") long id) {
        log.info("Request for getting picture  for message {} ", id);
        Optional<String> result = messageService.getPicture(id);
        if (result.isPresent()) return ResponseEntity.ok(result.get());
        else
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Post  maybe NULL ... or no such message"),
                    HttpStatus.BAD_REQUEST);
    }
}
