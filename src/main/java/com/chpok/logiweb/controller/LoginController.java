package com.chpok.logiweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @PostMapping("/login")
    String login() {
        return "redirect:employeePage";
    }

}
