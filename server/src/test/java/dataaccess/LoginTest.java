package dataaccess;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.LoginService;


import static org.junit.jupiter.api.Assertions.*;
class LoginTest {
    private AuthSQL authSQL;
    private UserSQL userSQL;
    private LoginService loginService;


    @BeforeEach
    void setUp() throws ResponseException, DataAccessException{
        userSQL = new UserSQL();
        authSQL = new AuthSQL();
        loginService = new LoginService(userSQL, authSQL);
    }

    @Test
    void loginTestPositive() throws ResponseException, DataAccessException {
        String hashedPassword = BCrypt.hashpw("cheese", BCrypt.gensalt());
        UserData userInfo = new UserData("Feathers McGraw", hashedPassword, "Chicken@gmail.com");
        userSQL.addUser(userInfo);
        UserData login = new UserData("Feathers McGraw", "cheese", "Chicken@gmail.com");
        AuthData authToken = loginService.login(login);
        assertEquals("Feathers McGraw", authToken.getUsername());
    }

    @Test
    void logoutTestNegative() {
        assertThrows(ResponseException.class, () -> loginService.login(null));
    }
}
