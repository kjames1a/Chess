package dataaccess;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import exceptions.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LogoutService;


import static org.junit.jupiter.api.Assertions.*;
class LogoutTest {
    private AuthSQL authSQL;
    private LogoutService logoutService;


    @BeforeEach
    void setUp() throws ResponseException, DataAccessException{
        authSQL = new AuthSQL();
        logoutService = new LogoutService(authSQL);
    }

    @Test
    void logoutTestPositive() throws ResponseException, DataAccessException {
        AuthData token = new AuthData("hdjkaj", "Feathers McGraw");
        authSQL.addAuthToken(token);
        logoutService.logout("hdjkaj");
        assertNull(authSQL.getAuthToken("hdjkaj"));
    }

    @Test
    void logoutTestNegative() {
        assertThrows(ResponseException.class, () -> logoutService.logout("bad token"));
    }
}
