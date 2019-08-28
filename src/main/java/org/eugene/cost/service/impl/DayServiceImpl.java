package org.eugene.cost.service.impl;

import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayServiceImpl implements IDayService {
    @Override
    public Day getDayByDate(Session session, LocalDate date) {
        for (Day day : session.getDays()){
            if(day.getDate().isEqual(date)){
                return day;
            }
        }
        return null;
    }

    @Override
    public List<Day> getAllDays(Session session) {
        return session.getDays();
    }

    @Override
    public List<Day> getAllDays(Session session, boolean closeDay) {
        return session.getDays().stream()
                .filter(day -> day.isClose() == closeDay)
                .collect(Collectors.toList());
    }

    @Override
    public void closeDay(Day day) {
        day.setClose(true);
    }

    @Override
    public void resumeDay(Day day) {
        day.setClose(false);
    }
}
