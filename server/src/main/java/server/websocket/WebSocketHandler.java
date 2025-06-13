package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ErrorResponse;
import exceptions.ResponseException;
import exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.websocket.commands.ConnectCommand;
import server.websocket.commands.LeaveCommand;
import server.websocket.commands.MakeMoveCommand;
import server.websocket.commands.ResignCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;




@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, DataAccessException, Exception {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            String username = getUser(command.getAuthToken());
            switch (command.getCommandType()) {
                case CONNECT -> {
                    ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
                    connect(session, username, connectCommand);
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(session, username, makeMoveCommand);
                }
                case LEAVE -> {
                    LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                    leaveGame(session, username, leaveCommand);
                }
                case RESIGN -> {
                    ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                    resign(session, username, resignCommand);
                }
            }
        } catch (UnauthorizedException ex) {
            errorMessage(session, new Gson().toJson(new ErrorResponse(400, "Error: Unauthorized")));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(session, new Gson().toJson(new ErrorResponse(400, "Error: Unauthorized")));
        }
    }

    public String getUser(String authToken) throws DataAccessException, ResponseException, UnauthorizedException {
        AuthSQL authData = new AuthSQL();
        return authData.getUserName(authToken);
    }

    public void connect(Session session, String userName, ConnectCommand connectCommand)  throws ResponseException {
        try {
            int gameID= connectCommand.getGameID();
            String userColor = connectCommand.getUserColor();
            connections.add(userName, session);
            GameSQL gameSQL = new GameSQL();
            GameData gameData = gameSQL.getGame(gameID);
            if (gameData == null) {
                throw new ResponseException(400, "Error: Game does not exist.");
            }
            ChessGame chessGame = gameData.getGame();
            if (chessGame == null) {
                throw new ResponseException(400, "Error: Game does not exist.");
            }
            String authToken = connectCommand.getAuthToken();
            AuthSQL authSQL = new AuthSQL();
            AuthData authData = authSQL.getAuthToken(authToken);
            if (authData == null) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errorMessage.setErrorMessage("Error: unauthorized");
                Connection connection = new Connection(userName, session);
                String loadMessage = new Gson().toJson(errorMessage);
                connection.send(loadMessage);
            }
            ServerMessage loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.setChessGame(chessGame);
            Connection connection = new Connection(userName, session);
            String loadMessage = new Gson().toJson(loadGame);
            connection.send(loadMessage);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            if (!userName.equals(gameData.getBlackUsername()) && !userName.equals(gameData.getWhiteUsername()) && gameData.getGameID() == gameID) {
                notification.setMessage(String.format("%s joined game as an observer.", userName));
                String json = new Gson().toJson(notification);
                connections.serverBroadcast(userName, json);
            } else {
                notification.setMessage(String.format("%s is playing on the %s team.", userName, userColor));
                String json = new Gson().toJson(notification);
                connections.serverBroadcast(userName, json);
            }
        } catch (Exception ex) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Error: Unauthorized");
            try {
                Connection connection = new Connection(userName, session);
                String loadMessage = new Gson().toJson(errorMessage);
                connection.send(loadMessage);
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }


    public void makeMove(Session session, String userName, MakeMoveCommand makeMoveCommand) throws ResponseException {
        try {
              int gameID = makeMoveCommand.getGameID();
              GameData gameData = gameDataHelper(gameID);
              GameSQL gameSQL = new GameSQL();
            if (gameSQL.gameEnd(gameID)) {
                Connection connection = new Connection(userName, session);
                ServerMessage gameEndErrorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                gameEndErrorMessage.setErrorMessage("Error: Game has ended.");
                String loadMessage = new Gson().toJson(gameEndErrorMessage);
                connection.send(loadMessage);
                return;
            }
            ChessGame chessGame = gameData.getGame();
            if (!gameData.getBlackUsername().equals(userName) && !gameData.getWhiteUsername().equals(userName)) {
                ServerMessage observerErrorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                observerErrorMessage.setErrorMessage("Error: Observers cannot resign from the game.");
                Connection connection = new Connection(userName, session);
                String loadMessage = new Gson().toJson(observerErrorMessage);
                connection.send(loadMessage);
                return;
            }
            Collection<ChessMove> validMoves = chessGame.validMoves(makeMoveCommand.getMoveDescription().getStartPosition());
            boolean validMove = validMoves.contains(makeMoveCommand.getMoveDescription());
            if (!validMove) {
                ServerMessage notValidMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                notValidMessage.setErrorMessage("Error: Move is not valid.");
                Connection connection = new Connection(userName, session);
                String message = new Gson().toJson(notValidMessage);
                connection.send(message);
                return;
            }
            ChessGame.TeamColor teamColor;
            if (gameData.getBlackUsername().equals(userName)) {
                teamColor = ChessGame.TeamColor.BLACK;
            } else if (gameData.getWhiteUsername().equals(userName)) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else {
                ServerMessage playerErrorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                playerErrorMessage.setErrorMessage("Error: not a player");
                Connection connection = new Connection(userName, session);
                String message = new Gson().toJson(playerErrorMessage);
                connection.send(message);
                return;
            }
            if (!chessGame.getTeamTurn().equals(teamColor)) {
                ServerMessage wrongTurnErrorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                wrongTurnErrorMessage.setErrorMessage("Error: Wait for your turn.");
                Connection connection = new Connection(userName, session);
                String message = new Gson().toJson(wrongTurnErrorMessage);
                connection.send(message);
                return;
            }
            chessGame.makeMove(makeMoveCommand.getMoveDescription());
            gameSQL.update(gameData);
            ServerMessage loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.setChessGame(chessGame);
            String loadMessage = new Gson().toJson(loadGame);
            connections.serverBroadcast("", loadMessage);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            ChessGame.TeamColor team = ChessGame.TeamColor.BLACK;
            switch (teamColor) {
                case BLACK -> team = ChessGame.TeamColor.WHITE;
                case WHITE -> team = ChessGame.TeamColor.BLACK;
            }
            if (chessGame.isInCheckmate(team)) {
                notification.setMessage("Checkmate");
                gameData.setGameEnd(true);
                new GameSQL().update(gameData);
            } else if (chessGame.isInStalemate(team)) {
                notification.setMessage("Stalemate");
                gameData.setGameEnd(true);
                new GameSQL().update(gameData);
            } else if (chessGame.isInCheck(team)) {
                notification.setMessage(userName + " is in check");
            } else {
                notification.setMessage(String.format("%s moved %s.", userName, makeMoveCommand));
            }
            String json = new Gson().toJson(notification);
            connections.serverBroadcast(userName, json);
        } catch (Exception ex) {
            exceptionHelper(session, userName, ex);
        }
    }

    public void leaveGame(Session session, String userName, LeaveCommand leaveCommand) throws ResponseException {
        try {
            int gameID = leaveCommand.getGameID();
            GameData gameData = gameDataHelper(gameID);
            if (userName.equals(gameData.getWhiteUsername())) {
                gameData.setWhiteUsername(null);
            } else if (userName.equals(gameData.getBlackUsername())) {
                gameData.setBlackUsername(null);
            }
            GameSQL gameSQL = new GameSQL();
            gameSQL.update(gameData);
            connections.remove(userName);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            notification.setMessage(String.format("%s has left the game.", userName));
            String json = new Gson().toJson(notification);
            connections.serverBroadcast(userName, json);
        } catch (Exception ex) {
            exceptionHelper(session, userName, ex);
        }
    }

    public void resign(Session session, String userName, ResignCommand resignCommand) throws ResponseException {
        try {
            int gameID = resignCommand.getGameID();
            GameData gameData = gameDataHelper(gameID);
            if (!userName.equals(gameData.getBlackUsername()) && !userName.equals(gameData.getWhiteUsername())) {
                ServerMessage observerErrorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                observerErrorMessage.setErrorMessage("Error: Observers cannot resign from the game.");
                Connection connection = new Connection(userName, session);
                String loadMessage = new Gson().toJson(observerErrorMessage);
                connection.send(loadMessage);
                return;
            }
            GameSQL gameSQL = new GameSQL();
            if (gameSQL.gameEnd(gameID)) {
                ServerMessage resignErrorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                resignErrorMessage.setErrorMessage("Error: A user has already resigned from the game.");
                Connection connection = new Connection(userName, session);
                String loadMessage = new Gson().toJson(resignErrorMessage);
                connection.send(loadMessage);
                return;
            }
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            notification.setMessage(String.format("%s resigned from the game.", userName));
            gameData.setGameEnd(true);
            resignCommand.setPlayerResign(true);
            gameSQL.update(gameData);
            String resignMessage = new Gson().toJson(notification);
            Connection connection = new Connection(userName, session);
            connection.send(resignMessage);
            String json = new Gson().toJson(notification);
            connections.serverBroadcast(userName, json);
        } catch (Exception ex) {
            exceptionHelper(session, userName, ex);
        }
    }

    private GameData gameDataHelper(int gameID) throws ResponseException, DataAccessException {
        GameSQL gameSQL = new GameSQL();
        GameData gameData = gameSQL.getGame(gameID);
        if (gameData == null) {
            throw new ResponseException(400, "Error: Game does not exist.");
        }
        if (gameData.getGame() == null) {
            throw new ResponseException(400, "Error: Game does not exist.");
        }
        return gameData;
    }

    public void errorMessage(Session session, String msg) throws Exception {
        session.getRemote().sendString(msg);
    }

    public void exceptionHelper(Session session, String userName, Exception ex) throws ResponseException {
        try {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage(ex.getMessage());
                Connection connection = new Connection(userName, session);
                String message = new Gson().toJson(errorMessage);
                connection.send(message);
            } catch (IOException error) {
                throw new ResponseException(500, error.getMessage());
            }
            throw new ResponseException(500, ex.getMessage());
        }
    }




