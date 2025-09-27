package pn.back.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pn.back.entities.Message;
import pn.back.entities.MessagePageData;
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

    @GetMapping("/api/posts")
    public MessagePageData seshowAllPG(
            @RequestParam(value = "pageNumber", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "1") int pageSize,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {

        if (pageSize < 1) return null;
        log.info(" \n\n-------\nRequest for showing   messages by criteria title or content has string {} page {} line to {} line",
                search, page, page + pageSize
        );

        return messageService.showAllPG(page, pageSize, search);
    }

}
