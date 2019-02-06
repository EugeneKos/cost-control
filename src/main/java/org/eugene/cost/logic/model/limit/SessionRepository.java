package org.eugene.cost.logic.model.limit;

import org.eugene.cost.logic.model.Repository;

import java.util.ArrayList;
import java.util.List;

public class SessionRepository implements Repository {
    private List<Session> sessions = new ArrayList<>();

    public void addSession(Session session){
        sessions.add(session);
    }

    public void removeSession(int index){
        sessions.remove(index);
    }

    public Session getSession(int index){
        return sessions.get(index);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    @Override
    public String getName() {
        return "sessions";
    }
}
