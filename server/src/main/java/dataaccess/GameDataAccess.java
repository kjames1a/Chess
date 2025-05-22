package dataaccess;

import model.GameData;

import java.util.Collection;


    public interface GameDataAccess {
        int addGame(String gameData) throws DataAccessException;

        Collection<GameData> listGame() throws DataAccessException;

        GameData getGame(int gameID) throws DataAccessException;

        void deleteAllGames() throws DataAccessException;
    }

