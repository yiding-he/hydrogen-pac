package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.config.CookieConfig;
import com.hyd.hydrogenpac.services.UserService;
import com.hyd.hydrogenpac.utils.Cookies;
import com.hyd.hydrogenpac.utils.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger LOG = LoggerFactory.getLogger(SessionInterceptor.class);

    @Autowired
    CookieConfig cookieConfig;

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        Cookies cookies = Cookies.from(request);
        String token = cookies.getCookieValue("token");

        if (token == null) {
            LOG.info("Generating new token");
            token = RandomStringGenerator.generate(40, true, true, true);
            response.addCookie(Cookies.newCookie(
                    "token", token, cookieConfig.getDomain(), cookieConfig.getExpiry()));
        }

        if (o instanceof HandlerMethod) {
            Object controller = ((HandlerMethod) o).getBean();
            if (controller instanceof AbstractController) {
                AbstractController c = (AbstractController) controller;
                c.setToken(token);
                c.setUser(userService.getLoggedInUser(token));
                c.setRequestUrl(request.getRequestURL().toString());
            }
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
