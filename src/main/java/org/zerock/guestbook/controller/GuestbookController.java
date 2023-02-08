package org.zerock.guestbook.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

    @GetMapping({"/","/list"})
    public String list() {
        log.info("list...........................");
        return "/geustbook/list";
    }

}
