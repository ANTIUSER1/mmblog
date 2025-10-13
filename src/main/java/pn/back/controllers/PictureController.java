/**
 * контрлллер работы с картинками
 */
package pn.back.controllers;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pn.back.entities.Message;
import pn.back.services.MessageService;
import pn.back.utils.ControllerUtil;

import java.io.IOException;
import java.util.Optional;

import static pn.back.utils.ControllerUtil.getResponseEntity;

@RestController
@Slf4j
public class PictureController {
    @Autowired
    private MessageService messageService;

    @PutMapping(ControllerUtil.ALL_POSTS_API + "/{id}/image")
    public ResponseEntity<?> addPicture(
            @Nullable @RequestParam("file") MultipartFile file,
            @PathVariable("id") long id
    ) throws IOException {
        if (file != null && file.getBytes().length > 0) {
            log.info("Request for Updating picture for message {} ", id);
            Optional<Message> message = messageService.addPicture(file, id);
            if (message.isPresent()) return ResponseEntity.ok(message.get());
        }
        return getResponseEntity(id, Optional.empty(), HttpStatus.BAD_REQUEST, " or File not defined");
    }


    @GetMapping(ControllerUtil.ALL_POSTS_API + "/{id}/image")
    public ResponseEntity<?> getPicture(@PathVariable("id") long id) {
        log.info("Request for getting picture  for message {} ", id);
        Optional<String> result = messageService.getPicture(id);
        if (result.isPresent()) return ResponseEntity.ok(result);
        return getResponseEntity(id, Optional.empty(), HttpStatus.BAD_REQUEST,
                " or File not defined");
    }
}
