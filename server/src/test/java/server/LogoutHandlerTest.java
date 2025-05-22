package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import exceptions.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LogoutService;


import static org.junit.jupiter.api.Assertions.*;
class LogoutHandlerTest {
    private AuthDAO authDAO;
    private LogoutService logoutService;


    @BeforeEach
    void setUp() {
        authDAO = new AuthDAO();
        logoutService = new LogoutService(authDAO);
    }

    @Test
    void logoutTestPositive() throws ResponseException, DataAccessException {
        AuthData token = new AuthData("ajkkjhjks", "Feathers McGraw");
        authDAO.addAuthToken(token);
        logoutService.logout("ajkkjhjks");
        assertNull(authDAO.getAuthToken("ajkkjhjks"));
    }

    @Test
    void logoutTestNegative() {
        assertThrows(ResponseException.class, () -> logoutService.logout(null));
    }
}