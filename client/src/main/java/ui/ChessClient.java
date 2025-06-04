package ui;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import chess.ChessGame;
import model.*;
import exceptions.ResponseException;
import server.ServerFacade;

public class ChessClient {
    private AuthData authData;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            Set<String> cmds = Set.of("register","r", "signIn", "s", "list", "l", "create", "c", "join", "j",
                    "watch", "w", "logout", "quit", "q", "help", "h");
            if (!cmds.contains(cmd)) {
                throw new ResponseException(400, "Please choose one of the options or 'h' to receive help");
            }
            return switch (cmd) {
                case "register", "r" -> register(params);
                case "signIn", "s" -> signIn(params);
                case "list", "l" -> listGames();
                case "create", "c" -> createGame(params);
                case "join", "j" -> joinGame(params);
                case "watch", "w" -> watchGame(params);
                case "logout" -> logOut();
                case "quit", "q" -> "quit";
                case "help", "h" -> help();
                default -> " ";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var userName = params[0];
            var password = params[1];
            var email = params[2];
            var registeredUser = new UserData(userName, password, email);
            authData = server.createUser(registeredUser);
            state = State.SIGNEDIN;
            return String.format("You registered as %s", authData.getUsername());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String signIn(String... params) throws ResponseException {
        if (params.length == 2) {
            var userName = params[0];
            var password = params[1];
            var user = new UserData(userName, password, null);
            authData = server.loginUser(user);
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", authData.getUsername());
        }
        throw new ResponseException(400, "Expected: <yourUserName> <password>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var gameName = params[0];
            ChessGame chessGame = new ChessGame();
            var game = new GameData(0, null, null, gameName, chessGame);
            server.createGame(game, authData.getAuthToken());
            return String.format("Successfully created %s", gameName);
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        Collection<GameData> games = server.listGames(authData.getAuthToken());
        var result = new StringBuilder();
        if (games.isEmpty()) {
            throw new ResponseException(400, "No games have been created");
        }
        for (var game : games) {
            var gameName = game.getGameName();
            var gameID = game.getGameID();
            var whiteUser = game.getWhiteUsername();
            var blackUser = game.getBlackUsername();
            if (whiteUser == null) {
                whiteUser = "No white player";
            }
            if (blackUser == null) {
                blackUser = "No black player";
            }
            result.append(String.format("%s. Game Name: %s | White Username: %s | Black Username: %s \n",
                    gameID, gameName, whiteUser, blackUser));
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            try {
                var gameID = Integer.parseInt(params[0]);
                var playerColor = params[1].toUpperCase();
                Collection<GameData> games = server.listGames(authData.getAuthToken());
                GameData joinGame = null;
                for (GameData game : games) {
                    if (game.getGameID() == gameID) {
                        joinGame = game;
                        break;
                    }
                }
                var joinedGame = new JoinData(gameID, playerColor, joinGame.getGameName());
                server.joinGame(joinedGame, authData.getAuthToken());
                if (playerColor.equals("WHITE")) {
                    new ChessBoard().ChessBoardWhite();
                } else {
                    new ChessBoard().ChessBoardBlack();
                }
                return String.format("Joined game %s as %s team", joinGame.getGameName(), playerColor);
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: <game id> <color>");
    }

    public String watchGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var gameID = Integer.parseInt(params[0]);
            Collection<GameData> games = server.listGames(authData.getAuthToken());
            GameData gameName = null;
            for (GameData game : games) {
                if (game.getGameID() == gameID) {
                    gameName = game;
                    break;
                }
            }
            if (gameName == null) {
                throw new ResponseException(400, "Game has yet to be created");
            }
            var joinedGame = new JoinData(gameID, null, gameName.getGameName());
            server.watchGame(joinedGame, authData.getAuthToken());
            new ChessBoard().ChessBoardWhite();
            return String.format("Watching game %s", gameName.getGameName());
        }
        throw new ResponseException(400, "Expected <game id>");
    }

    public String logOut() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        return String.format("%s logged out", authData.getUsername());
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    Options:
                    Sign in as an existing user: "s", "signin" <USERNAME> <PASSWORD>
                    Register a new user: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                    Exit the program: "q", "quit"
                    Print this message: "h", "help"
                    """;
        }
        return """
                Options:
                List current games: "l", "list"
                Create a new game: "c", "create" <GAME NAME>
                Join a game: "j", "join" <GAME ID> <COLOR>
                Watch a game: "w", "watch" <GAME ID>
                Logout: "logout"
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}

