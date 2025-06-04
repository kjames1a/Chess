package server;

import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ResponseException;
import dataaccess.DataAccessException;
import spark.*;

import java.util.Map;

public class Server {
    private UserSQL userSQL;
    private AuthSQL authSQL;
    private GameSQL gameSQL;
    private RegisterHandler registerHandler;
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;
    private CreateGameHandler createGameHandler;
    private ListGameHandler listGameHandler;
    private JoinGameHandler joinGameHandler;
    private WatchGameHandler watchGameHandler;


    public Server() {
        try {
            this.userSQL = new UserSQL();
            this.authSQL = new AuthSQL();
            this.gameSQL = new GameSQL();
            this.registerHandler = new RegisterHandler(userSQL, authSQL);
            this.loginHandler = new LoginHandler(userSQL, authSQL);
            this.logoutHandler = new LogoutHandler(authSQL);
            this.createGameHandler = new CreateGameHandler(gameSQL, authSQL);
            this.listGameHandler = new ListGameHandler(gameSQL, authSQL);
            this.joinGameHandler = new JoinGameHandler(gameSQL, authSQL);
            this.watchGameHandler = new WatchGameHandler(gameSQL, authSQL);
        } catch (ResponseException | DataAccessException ex) {
            throw new RuntimeException("Server failed", ex);
        }
    }

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

        Spark.get("/game/:gameID", watchGameHandler::handle);

        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

private Object dbRoute(Request req, Response res) throws ResponseException {
    try {
        authSQL.deleteAllAuthTokens();
        userSQL.deleteAllUsers();
        gameSQL.deleteAllGames();
    } catch (DataAccessException ex) {
        throw new ResponseException(500, "Error: Problem contacting database");
    }
    return postList(req, res);
}

private Object postList(Request req, Response res) {
    res.type("application/json");
    return new Gson().toJson(Map.of("name", "Cleared"));
}

    public static void main(String [] args) throws ResponseException, DataAccessException {
        Server server= new Server();
        server.run (8080);
        System.out.println("Connected to server");
    }

    public static void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }
}
