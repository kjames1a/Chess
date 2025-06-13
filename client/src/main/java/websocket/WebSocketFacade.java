package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.NotificationMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    private final String authToken;
    private final int gameID;




    public WebSocketFacade(String url, NotificationHandler notificationHandler, String authToken, int gameID) throws ResponseException {
        this.gameID = gameID;
        this.authToken = authToken;
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void playGame() throws ResponseException {
        try {
            var playGame = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(playGame));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void watchGame(String userName) throws ResponseException {
        try {
            var watchGame = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(watchGame));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }



}