package dataaccess;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterService;


import static org.junit.jupiter.api.Assertions.*;
class RegisterTest {
    private AuthSQL authSQL;
    private UserSQL userSQL;
    private RegisterService registerService;


    @BeforeEach
    void setUp() throws ResponseException, DataAccessException{
        userSQL = new UserSQL();
        authSQL = new AuthSQL();
        registerService = new RegisterService(userSQL, authSQL);
    }

    @Test
    void registerTestPositive() throws ResponseException, DataAccessException {
        UserData user = new UserData("Feathers", "cheese", "Chicken@gmail.com");
        AuthData authToken = registerService.register(user);
        assertEquals("Feathers", authToken.getUsername());
    }

    @Test
    void registerTestNegative() throws ResponseException, DataAccessException {
        UserData user = new UserData("Shawn", "sheep", "sheeeep1@gmail.com");
        registerService.register(user);
        assertThrows(ResponseException.class, () -> registerService.register(user));
    }
}
