package com.example.mypetlife.repository;

import com.example.mypetlife.entity.Calendar;
import com.example.mypetlife.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByDate(LocalDate date);
    List<Calendar> findAllByUserId(User user);
}
