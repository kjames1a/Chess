package dataaccess;

import model.GameData;

import java.util.Collection;


    public interface GameDataAccess {
        int addGame(String gameName) throws DataAccessException;

        Collection<GameData> listGame() throws DataAccessException;

        GameData getGame(int gameID) throws DataAccessException;

        void joinGame(GameData game) throws DataAccessException;

        void deleteAllGames() throws DataAccessException;
    }

