package com.tmk2003.zuultesting.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Basic routes for the application
 */
@Controller
public class IndexController {

    /**
     * Default route to the application
     * @return - Thymeleaf template name
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
