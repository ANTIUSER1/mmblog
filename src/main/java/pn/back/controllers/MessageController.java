package pn.back.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pn.back.entities.Message;
import pn.back.services.MessageService;

import java.util.List;

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

}
