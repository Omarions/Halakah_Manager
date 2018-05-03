package com.omarionapps.halaka.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Omar on 15-Apr-17.
 */
@Controller
public class MyErrorController implements ErrorController {

    private final String ERROR_PATH = "/error";
    private final String ERROR_TEMPLATE = "error/404";

    @RequestMapping("/404")
    public String error(){
        return ERROR_TEMPLATE;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

