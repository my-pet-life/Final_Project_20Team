package com.example.mypetlife.controller.view;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ViewController {

    @GetMapping("/")
    public String root(){
        return "main";
    }

    @GetMapping("/main")
    public String main(){
        return "main";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "/register";
    }

    @GetMapping("/main-success")
    public String mainSuccess(){
        return "mainSuccess";
    }

    @GetMapping("/calendar")
    public String calendar(){
        return "calendar";
    }

    @GetMapping("/schedules")
    public String schedules(){
        return "schedules";
    }

    @GetMapping("/create-schedule")
    public String createSchedule(){
        return "createSchedule";
    }
}