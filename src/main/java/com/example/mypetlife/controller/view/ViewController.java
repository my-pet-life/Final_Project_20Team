package com.example.mypetlife.controller.view;

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
        return "register";
    }

    @GetMapping("/main-success")
    public String mainSuccess(){
        return "mainSuccess";
    }

    @GetMapping("/calendar")
    public String calendar(){
        return "calendar/calendar";
    }

    @GetMapping("/schedules")
    public String schedules(){
        return "calendar/schedules";
    }

    @GetMapping("/create-schedule")
    public String createSchedule(){
        return "calendar/createSchedule";
    }

    @GetMapping("/update-schedule")
    public String updateSchedule(){
        return "calendar/updateSchedule";
    }

    @GetMapping("/read-alarm-schedule")
    public String readAlarmSchedule(){
        return "calendar/readAlarmSchedule";
    }

    @GetMapping("/read-date-schedule")
    public String readDateSchedule(){
        return "calendar/readDateSchedule";
    }

    @GetMapping("/community")
    public String community(){return "community/community";}

    @GetMapping("/article")
    public String article(){return "community/article";}
}