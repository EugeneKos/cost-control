package org.eugene.cost.service;

import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;

import java.time.LocalDate;

public interface IDayService {
    int getNumOpenDays(Session session);

    Day getDayByDate(Session session, LocalDate date);

    void closeDay(Day day);
    void resumeDay(Day day);
}
