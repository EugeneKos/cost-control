package org.eugene.cost.service;

import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;

import java.time.LocalDate;
import java.util.List;

public interface IDayService {
    Day getDayByDate(Session session, LocalDate date);

    List<Day> getAllDays(Session session);
    List<Day> getAllDays(Session session, boolean closeDay);

    void closeDay(Day day);
    void resumeDay(Day day);
}
