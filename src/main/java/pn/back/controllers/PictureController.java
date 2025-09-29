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

import java.io.IOException;
import java.util.Optional;

import static pn.back.utils.ControllerUtil.getResponseEntity;

@RestController
//@RequestMapping("/api")
@Slf4j
public class PictureController {
    @Autowired
    private MessageService messageService;

    @PutMapping("/api/posts/{id}/image")
    public ResponseEntity<?> addPicture(
            @Nullable @RequestParam("file") MultipartFile file,
            @PathVariable("id") long id
    ) throws IOException {
        System.out.println(" FILE == null " + file == null);


        if (file != null && file.getBytes().length > 0) {
            log.info("Request for Updating picture for message {} ", id);
            Optional<Message> message = messageService.addPicture(file, id);
            if (message.isPresent()) return ResponseEntity.ok(message.get());
        }
        return getResponseEntity(id, Optional.empty(), HttpStatus.BAD_REQUEST, " or File not defined");
    }


    @GetMapping("/api/posts/{id}/image")
    public String getPicture(@PathVariable("id") long id) {
        log.info("Request for getting picture  for message {} ", id);

        return messageService.getPicture(id);
    }
}
