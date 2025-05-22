package server;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;


import static org.junit.jupiter.api.Assertions.*;
class LoginHandlerTest {
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private LoginService loginService;


    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        loginService = new LoginService(userDAO, authDAO);
    }

    @Test
    void loginTestPositive() throws ResponseException, DataAccessException {
        UserData user = new UserData("Feathers McGraw", "cheese", "Chicken@gmail.com");
        userDAO.addUser(user);
        AuthData authToken = loginService.login(user);
        assertEquals("Feathers McGraw", authToken.getUsername());
    }

    @Test
    void logoutTestNegative() {
        assertThrows(ResponseException.class, () -> loginService.login(null));
    }
}