package com.example.mypetlife.controller.view;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/main-success")
    public String mainSuccess(HttpServletRequest request){
        String token = request.getParameter("accessToken");
        log.info("token: " + token);
        return "mainSuccess";
    }
}
