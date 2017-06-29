package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @author yiding.he
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(String user, String pass, HttpSession session) {
        if (userService.login(session, user, pass)) {
            return "redirect:main";
        } else {
            return "redirect:login";
        }
    }

    @GetMapping(value = {"/", "/main"})
    public String index(HttpSession session) {
        if (userService.isUserLoggedIn(session)) {
            return "main";
        } else {
            return "redirect:login";
        }
    }
}
