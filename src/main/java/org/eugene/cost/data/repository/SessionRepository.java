package org.eugene.cost.data.repository;

import org.eugene.cost.data.model.Session;
import org.eugene.cost.data.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class SessionRepository implements Repository {
    private List<Session> sessions = new ArrayList<>();

    public void addSession(Session session){
        sessions.add(session);
    }

    public void removeSession(Session session){
        sessions.remove(session);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    @Override
    public String getName() {
        return "sessions";
    }
}
