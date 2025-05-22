package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import model.GameData;
import spark.*;

import java.util.Map;

public class Server {
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();
    GameDAO gameDAO = new GameDAO();
    RegisterHandler registerHandler = new RegisterHandler(userDAO, authDAO);
    LoginHandler loginHandler = new LoginHandler(userDAO, authDAO);
    LogoutHandler logoutHandler = new LogoutHandler(authDAO);
    CreateGameHandler createGameHandler = new CreateGameHandler(gameDAO, authDAO);
    ListGameHandler listGameHandler = new ListGameHandler(gameDAO, authDAO);
    JoinGameHandler joinGameHandler = new JoinGameHandler(gameDAO, authDAO);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/session", loginHandler::handle);

        Spark.post("/user", registerHandler::handle);

        Spark.delete("/session", logoutHandler::handle);

        Spark.delete("/db", this::dbRoute);

        Spark.post("/game", createGameHandler::handle);

        Spark.get("/game", listGameHandler::handle);

        Spark.put("/game", joinGameHandler::handle);


        Spark.awaitInitialization();
        return Spark.port();
    }


private Object dbRoute(Request req, Response res) {
    authDAO.deleteAllAuthTokens();
    userDAO.deleteAllUsers();
    gameDAO.deleteAllGames();
    return postList(req, res);
}

private Object postList(Request req, Response res) {
    res.type("application/json");
    return new Gson().toJson(Map.of("name", authDAO));
}


    public static void main(String [] args) {
        Server server= new Server();
        server.run (8080);
        System.out.println("Connected to server");
    }

    public static void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
