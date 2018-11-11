package com.tmk2003.zuultesting.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    /**
     * Default route to the application error page
     * @return - Thymeleaf template name
     */
    @RequestMapping("/error")
    public String error() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
