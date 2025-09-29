package pn.back.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pn.back.entities.Message;
import pn.back.services.MessageService;

import java.io.IOException;

@RestController
//@RequestMapping("/api")
@Slf4j
public class PictureController {
    @Autowired
    private MessageService messageService;

    @PutMapping("/api/posts/{id}/image")
    public Message addPicture(
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") long id
    ) throws IOException {
        log.info("Request for Updating picture for message {} ", id);

        return messageService.addPicture(file, id);
    }


    @GetMapping("/api/posts/{id}/image")
    public String getPicture(@PathVariable("id") long id) {
        log.info("Request for getting picture  for message {} ", id);

        return messageService.getPicture(id);
    }
}
