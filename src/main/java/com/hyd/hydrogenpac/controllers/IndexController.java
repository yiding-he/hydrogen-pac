package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.beans.Proxy;
import com.hyd.hydrogenpac.beans.Result;
import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.oauth.OAuthChannel;
import com.hyd.hydrogenpac.oauth.OAuthEntry;
import com.hyd.hydrogenpac.oauth.OAuthService;
import com.hyd.hydrogenpac.oauth.OAuthServiceFactory;
import com.hyd.hydrogenpac.services.PatternsService;
import com.hyd.hydrogenpac.services.ProxyService;
import com.hyd.hydrogenpac.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yiding.he
 */
@Controller
public class IndexController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private PatternsService patternsService;

    @Autowired
    private OAuthServiceFactory oAuthServiceFactory;

    @GetMapping("/login")
    public String login(Model model) {

        OAuthChannel[] oAuthChannels = OAuthChannel.values();

        List<OAuthEntry> entryList = Arrays.stream(oAuthChannels)
                .map(oAuthServiceFactory::getOAuthService)
                .filter(OAuthService::isEnabled)
                .map(this::getOAuthEntry)
                .collect(Collectors.toList());

        model.addAttribute("loginEntries", entryList);
        return "login";
    }

    private OAuthEntry getOAuthEntry(OAuthService oAuthService) {
        return oAuthService.getOAuthEntry(getRequestUrl());
    }

    @GetMapping("/auth/callback/baidu")
    public String callbackBaidu(String code) throws IOException {

        OAuthService baiduOAuthService = oAuthServiceFactory.getOAuthService(OAuthChannel.Baidu);
        User user = baiduOAuthService.getUser(code, getRequestUrl());

        userService.onUserLoggedIn(user, getToken());
        return "redirect:../../main";
    }

    @GetMapping({"", "/"})
    public String index0() {
        return "redirect:main";
    }

    @GetMapping(value = {"/main"})
    public String index(Model model) {
        User user = getUser();

        if (user == null) {
            LOG.info("Token " + getToken() + " need login.");
            return "redirect:login";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("proxies", proxyService.getProxies(user));
            model.addAttribute("patterns", patternsService.getPatterns(user));
            return "main";
        }
    }

    @GetMapping("/proxy/add")
    public ModelAndView addProxy() {
        return new ModelAndView("proxy_info").addObject("title", "Add Proxy").addObject("action", "add");
    }

    @GetMapping("/proxy/edit")
    public ModelAndView editProxy() {
        return new ModelAndView("proxy_info").addObject("title", "Edit Proxy").addObject("action", "edit");
    }

    @PostMapping("/proxy/add")
    @ResponseBody
    public Result addProxy(String name, String type, String host, int port) {
        proxyService.addProxy(getUser(), new Proxy(name, type, host, port));
        return Result.success();
    }

    @PostMapping("/proxy/delete")
    @ResponseBody
    public Result deleteProxy(String name) {
        proxyService.deleteProxy(getUser(), name);
        return Result.success();
    }

    @PostMapping("/patterns/add")
    @ResponseBody
    public Result addPatterns(String name) {
        patternsService.addPatterns(getUser(), name);
        return Result.success();
    }
}
