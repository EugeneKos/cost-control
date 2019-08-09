package org.eugene.cost.service.impl;

import org.eugene.cost.data.Session;
import org.eugene.cost.service.IDayService;
import org.springframework.stereotype.Service;

@Service
public class DayServiceImpl implements IDayService {
    @Override
    public int getNumOpenDays(Session session) {
        return (int) session.getDays().stream().filter(day -> !day.isClose()).count();
    }
}
