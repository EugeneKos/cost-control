package org.eugene.cost.service;

import org.eugene.cost.data.Session;

import java.time.LocalDate;

public interface ISessionService {
    Session create(String limit, LocalDate beginDate, LocalDate finalDate);

    void update(Session session);

    void calculateMediumLimit(Session session);
    void autoCloseDays(Session session);
}
