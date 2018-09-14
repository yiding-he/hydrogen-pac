package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.beans.Proxy;
import com.hyd.hydrogenpac.beans.Result;
import com.hyd.hydrogenpac.services.ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yiding_he
 */
@Controller
public class ProxyController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyController.class);

    @Autowired
    private ProxyService proxyService;

    @GetMapping("/add_proxy")
    public String addProxy() {
        return "add_proxy";
    }

    @PostMapping("/add_proxy")
    public String addProxy(String proxyName, String proxyType, String proxyAddress) {
        proxyService.addProxy(getUser(), new Proxy(proxyName, proxyType, proxyAddress));
        return "redirect:main";
    }

    @PostMapping("/delete_proxy")
    @ResponseBody
    public Result deleteProxy(String name) {
        try {
            proxyService.deleteProxy(getUser(), name);
            return Result.success();
        } catch (Exception e) {
            LOG.error("", e);
            return Result.fail("删除失败:" + e.toString());
        }
    }
}
