package org.eugene.cost.cache;

import org.eugene.cost.data.Session;
import org.eugene.cost.file.FileManager;
import org.eugene.cost.service.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SessionCache {
    private Map<SessionDetail, Session> sessionCache = new HashMap<>();

    private FileManager<Session> fileManager;

    @Autowired
    public SessionCache(FileManager<Session> fileManager) {
        this.fileManager = fileManager;
    }

    public void addSession(Session session){
        SessionDetail detail = new SessionDetail(session.getLimit(), session.getBeginDate(), session.getFinalDate());
        sessionCache.put(detail, session);
    }

    public Session getSession(String limit, LocalDate beginDate, LocalDate finalDate){
        SessionDetail detail = new SessionDetail(limit, beginDate, finalDate);
        return sessionCache.get(detail);
    }

    public Collection<Session> getAllSessions(){
        return sessionCache.values();
    }

    @PostConstruct
    private void initialize(){
        Collection<Session> sessions = fileManager.loadAll(SessionUtils.SESSION_REGEXP, Session.class).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());

        sessions.forEach(this::addSession);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SessionDetail detail = (SessionDetail) o;
            return Objects.equals(limit, detail.limit) &&
                    Objects.equals(beginDate, detail.beginDate) &&
                    Objects.equals(finalDate, detail.finalDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(limit, beginDate, finalDate);
        }
    }
}
