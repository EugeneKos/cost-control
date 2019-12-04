package org.eugene.cost.cache;

import org.eugene.cost.data.Session;
import org.eugene.cost.data.SessionDetail;
import org.eugene.cost.file.FileManager;
import org.eugene.cost.service.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void deleteSession(Session session){
        SessionDetail detail = new SessionDetail(session.getLimit(), session.getBeginDate(), session.getFinalDate());
        sessionCache.remove(detail);
    }

    public Collection<Session> getAllSessions(){
        return sessionCache.values();
    }

    public void initialize(){
        Collection<Session> sessions = fileManager.loadAll(SessionUtils.SESSION_REGEXP, Session.class).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());

        sessions.forEach(this::addSession);
    }
}
