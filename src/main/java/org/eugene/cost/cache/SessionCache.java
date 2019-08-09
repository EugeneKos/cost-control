package org.eugene.cost.cache;

import org.eugene.cost.data.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionCache {
    private Map<SessionDetail, Session> sessionCache = new HashMap<>();

    public void addSession(Session session){
        SessionDetail detail = new SessionDetail(session.getLimit(), session.getBeginDate(), session.getFinalDate());
        if(!sessionCache.containsValue(session)){
            sessionCache.put(detail, session);
        }
    }

    public Session getSession(String limit, LocalDate beginDate, LocalDate finalDate){
        SessionDetail detail = new SessionDetail(limit, beginDate, finalDate);
        return sessionCache.get(detail);
    }

    private class SessionDetail {
        private final String limit;
        private final LocalDate beginDate;
        private final LocalDate finalDate;

        SessionDetail(String limit, LocalDate beginDate, LocalDate finalDate) {
            this.limit = limit;
            this.beginDate = beginDate;
            this.finalDate = finalDate;
        }

        public String getLimit() {
            return limit;
        }

        public LocalDate getBeginDate() {
            return beginDate;
        }

        public LocalDate getFinalDate() {
            return finalDate;
        }
    }
}
