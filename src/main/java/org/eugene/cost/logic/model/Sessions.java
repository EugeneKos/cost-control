package org.eugene.cost.logic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sessions implements Serializable {
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
}
