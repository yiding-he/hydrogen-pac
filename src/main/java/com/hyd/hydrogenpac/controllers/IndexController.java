package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.beans.PatternList;
import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.oauth.OAuthEntry;
import com.hyd.hydrogenpac.oauth.OAuthService;
import com.hyd.hydrogenpac.oauth.OAuthServiceFactory;
import com.hyd.hydrogenpac.oauth.OAuthServiceType;
import com.hyd.hydrogenpac.services.PacService;
import com.hyd.hydrogenpac.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yiding.he
 */
@Controller
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PacService pacService;

    @Autowired
    private OAuthServiceFactory oAuthServiceFactory;

    @GetMapping("/login")
    public String login(Model model) {

        List<OAuthEntry> entryList = Arrays.stream(OAuthServiceType.values())
                .map(type -> {
                    OAuthService oAuthService = oAuthServiceFactory.getOAuthService(type);
                    return oAuthService.getOAuthEntry();
                })
                .collect(Collectors.toList());

        model.addAttribute("loginEntries", entryList);
        return "login";
    }

    @GetMapping("/auth/callback/baidu")
    public String callbackBaidu(String code, Model model) throws IOException {

        OAuthService baiduOAuthService = oAuthServiceFactory.getOAuthService(OAuthServiceType.Baidu);
        User user = baiduOAuthService.getUser(code);
        model.addAttribute("user", user);

        return "auth_callback_baidu";
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
