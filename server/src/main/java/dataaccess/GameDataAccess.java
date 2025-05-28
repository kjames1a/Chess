package dataaccess;

import exceptions.ResponseException;
import model.GameData;

import java.util.Collection;


    public interface GameDataAccess {
        int addGame(String gameName, String whiteUsername, String blackUsername) throws DataAccessException, ResponseException;

        Collection<GameData> listGame() throws DataAccessException, ResponseException;

        GameData getGame(int gameID) throws DataAccessException, ResponseException;

        void deleteAllGames() throws DataAccessException, ResponseException;

        GameData update(GameData game) throws DataAccessException, ResponseException;
    }

