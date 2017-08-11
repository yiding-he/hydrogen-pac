package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.services.UserService;
import com.hyd.hydrogenpac.utils.Cookies;
import com.hyd.hydrogenpac.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * (description)
 * created at 2017/8/11
 *
 * @author yidin
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Value("${server.session.timeout}")
    private int sessionExpireSeconds;

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String token = Cookies.from(request).getCookieValue("token");
        if (token == null) {
            token = RandomStringGenerator.generate(40, true, true, true);
            response.addCookie(Cookies.newCookie("token", token, sessionExpireSeconds));
        }

        Object controller = ((HandlerMethod) o).getBean();
        if (controller instanceof AbstractController) {
            ((AbstractController) controller).setToken(token);
            ((AbstractController) controller).setUser(userService.getLoggedInUser(token));
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object o,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object o,
            Exception e) throws Exception {

    }
}
