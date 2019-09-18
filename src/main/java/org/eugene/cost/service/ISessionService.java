package org.eugene.cost.service;

import org.eugene.cost.data.Session;

import java.time.LocalDate;
import java.util.List;

public interface ISessionService {
    Session create(String limit, LocalDate beginDate, LocalDate finalDate);

    void update(Session session);

    void delete(Session session);

    List<Session> getAll();

    void calculateMediumLimit(Session session);
    void autoCloseDays(Session session);

    boolean isActive(Session session);
}
