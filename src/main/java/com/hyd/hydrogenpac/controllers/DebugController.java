package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.beans.Result;
import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yiding_he
 */
@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/all")
    public Result allUsers() {
        List<User> users = userService.findAll();
        return Result.success().put("users", users);
    }
}
