package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.SpringBootTestBase;
import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.oauth.OAuthChannel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * @author yiding_he
 */
public class UserServiceTest extends SpringBootTestBase {

    @Autowired
    private UserService userService;

    @Test
    public void testInsertUser() throws Exception {
        userService.saveUser(new User(OAuthChannel.None, "0", "none"));
    }

    @Test
    public void testFindUser() throws Exception {
        User user = userService.findById(OAuthChannel.None, "0");
        assertNotNull(user);
    }

    @Test
    public void testFindAll() throws Exception {
        userService.findAll().forEach(System.out::println);
    }
}