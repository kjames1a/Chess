package server;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;
import service.RegisterService;


import static org.junit.jupiter.api.Assertions.*;
class RegisterHandlerTest {
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private RegisterService registerService;


    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    void registerTestPositive() throws ResponseException, DataAccessException {
        UserData user = new UserData("Feathers", "cheese", "Chicken@gmail.com");
        AuthData authToken = registerService.register(user);
        assertEquals("Feathers", authToken.getUsername());
    }

    @Test
    void registerTestNegative() {
        assertThrows(ResponseException.class, () -> registerService.register(null));
    }
}