package service;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;


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
        String hashedPassword = BCrypt.hashpw("cheese", BCrypt.gensalt());
        UserData userInfo = new UserData("Feathers McGraw", hashedPassword, "Chicken@gmail.com");
        userDAO.addUser(userInfo);
        UserData login = new UserData("Feathers McGraw", "cheese", "Chicken@gmail.com");
        AuthData authToken = loginService.login(login);
        assertEquals("Feathers McGraw", authToken.getUsername());
    }

    @Test
    void logoutTestNegative() {
        assertThrows(ResponseException.class, () -> loginService.login(null));
    }
}