package websocket.messages;

import com.google.gson.Gson;

public record NotificationMessage(Type type, String message) {

    public enum Type {
        PLAYGAME,
        MOVEMADE,
        LEFTGAME,
        RESIGN
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
