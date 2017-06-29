package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.beans.PatternList;
import com.hyd.hydrogenpac.services.PacService;
import com.hyd.hydrogenpac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yiding.he
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private PacService pacService;

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
    public String index(HttpSession session, Model model) {
        if (userService.isUserLoggedIn(session)) {
            String user = userService.getUserName(session);
            List<PatternList> patternListSettings = pacService.getPatternLists(user);
            model.addAttribute("patternListSettings", patternListSettings);
            return "main";
        } else {
            return "redirect:login";
        }
    }
}
