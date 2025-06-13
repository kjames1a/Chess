package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public final ConcurrentHashMap<String, Integer> allGames = new ConcurrentHashMap<>();

    public void add(Session session, String authToken, int gameID) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
        allGames.put(authToken, gameID);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
        allGames.remove(authToken);
    }

    public void serverBroadcast(String excludeUserName, String notification, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            Integer gameUser = allGames.get(c.authToken);
            if (c.session.isOpen()) {
                if (gameUser == gameID) {
                    if (!c.authToken.equals(excludeUserName)) {
                        c.send(notification);
                    }
                }
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.authToken);
            allGames.remove(c.authToken);
        }
    }
}

