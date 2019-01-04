package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.Errors;
import com.hyd.hydrogenpac.beans.Proxy;
import com.hyd.hydrogenpac.beans.Result;
import com.hyd.hydrogenpac.services.PatternsService;
import com.hyd.hydrogenpac.services.ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yiding.he
 */
@Controller
public class IndexController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private PatternsService patternsService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping({"", "/"})
    public String index0() {
        return "redirect:main";
    }

    @GetMapping(value = {"/main"})
    public String index(Model model) {
        return "index";
/*
        User user = getUser();

        if (user == null) {
            LOG.info("Token " + getToken() + " need login.");
            return "redirect:login";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("proxies", proxyService.findProxies(user));
            model.addAttribute("patterns", patternsService.getPatterns(user));
            return "main";
        }
*/
    }

    @GetMapping("get_error")
    @ResponseBody
    public Result getErrorMessage(String error) {
        return Result.success().put("message", Errors.getErrorMessage(error));
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
    public ModelAndView addProxy(String name, String type, String host, int port) {
        if (proxyService.findProxyByName(getUser().getUserId(), name) != null) {
            return new ModelAndView("redirect:/proxy/add?error=1");
        }

        String userId = getUser().getUserId();
        proxyService.addProxy(new Proxy(userId, name, type, host, port));
        return new ModelAndView("redirect:/main");
    }

    @PostMapping("/proxy/delete")
    @ResponseBody
    public Result deleteProxy(String name) {
        final String userId = getUser().getUserId();
        proxyService.deleteProxy(userId, name);
        return Result.success();
    }

    @PostMapping("/patterns/add")
    @ResponseBody
    public Result addPatterns(String name) {
        patternsService.addPatterns(getUser(), name);
        return Result.success();
    }
}
