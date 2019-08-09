package org.eugene.cost.service.impl;

import org.eugene.cost.cache.SessionCache;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.IDayService;
import org.eugene.cost.service.ISessionService;
import org.eugene.cost.util.Calculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionServiceImpl implements ISessionService {
    private SessionCache sessionCache;
    private IDayService dayService;
    private IBuyService buyService;

    @Autowired
    public SessionServiceImpl(SessionCache sessionCache, IDayService dayService, IBuyService buyService) {
        this.sessionCache = sessionCache;
        this.dayService = dayService;
        this.buyService = buyService;
    }

    @Override
    public Session create(String limit, LocalDate beginDate, LocalDate finalDate) {
        if(!checkDates(beginDate, finalDate)){
            return null;
        }
        Session session = new Session(limit, beginDate, finalDate, initDays(beginDate, finalDate));
        sessionCache.addSession(session);
        return session;
    }

    private boolean checkDates(LocalDate beginDate, LocalDate finalDate){
        if(!beginDate.isEqual(LocalDate.now())){
            return false;
        }
        return beginDate.isBefore(finalDate);
    }

    private List<Day> initDays(LocalDate beginDate, LocalDate finalDate){
        List<Day> days = new ArrayList<>();
        LocalDate current = beginDate;
        days.add(new Day(current));
        while (!current.isEqual(finalDate)){
            current = current.plusDays(1);
            days.add(new Day(current));
        }
        return days;
    }

    @Override
    public void calculateMediumLimit(Session session) {
        int numOpenDays = dayService.getNumOpenDays(session);
        List<Day> days = session.getDays();
        String rateByNonLimitBuys = buyService.getRateByNonLimitBuys(days);
        String diff = Calculate.minus(session.getLimit(), rateByNonLimitBuys);
        String mediumLimit = Calculate.divide(diff, String.valueOf(numOpenDays));
        days.forEach(day -> setMediumLimit(day, mediumLimit));
    }

    private void setMediumLimit(Day day, String mediumLimit){
        if(!day.isClose()){
            day.setLimit(mediumLimit);
        }
    }

    @Override
    public void autoCloseDays(Session session) {
        List<Day> days = session.getDays();
        for (Day day : days){
            if(day.getDate().isBefore(LocalDate.now())){
                day.setClose(true);
            }
        }
        calculateMediumLimit(session);
    }
}
