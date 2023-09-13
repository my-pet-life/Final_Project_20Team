package com.example.mypetlife.init.calendar;

import com.example.mypetlife.entity.Calendar;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.repository.CalendarRepository;
import com.example.mypetlife.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class InitCalendar {
    private final InitCalendarService initCalendarService;

    @PostConstruct
    public void init(){
        initCalendarService.createSchedule();
    }

    @Service @RequiredArgsConstructor @Transactional
    static class InitCalendarService {
        private final CalendarRepository calendarRepository;
        private final UserRepository userRepository;

        public void createSchedule(){
            User kim  = userRepository.findByEmail("kim@naver.com").get();

            Calendar calendar1 = new Calendar();
            calendar1.setId(1L);
            calendar1.setUserId(kim);
            calendar1.setDate(LocalDate.parse("2023-09-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            calendar1.setStartTime("19:00");
            calendar1.setEndTime("20:00");
            calendar1.setTitle("병원 방문");
            calendar1.setContent("정기 검진");
            calendar1.setLocation("xx 동물 병원");
            calendar1.setAlarm(-1);
            calendarRepository.save(calendar1);

            Calendar calendar2 = new Calendar();
            calendar2.setId(2L);
            calendar2.setUserId(kim);
            calendar2.setDate(LocalDate.parse("2023-09-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            calendar2.setStartTime("17:00");
            calendar2.setEndTime("18:00");
            calendar2.setTitle("산책");
            calendar2.setLocation("집 앞 공원에서");
            calendar2.setAlarm(-1);
            calendarRepository.save(calendar2);
        }
    }
}