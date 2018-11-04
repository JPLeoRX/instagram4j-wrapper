package com.tekleo.instagram4jwrapper;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoggedUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Leo Ertuna
 * @since 04.10.2018 02:02
 */
public class LoginTest {
    private static final Logger LOGGER = Logger.getLogger(LoginTest.class);
    private static final String USERNAME = "jpleorx1234";
    private static final String PASSWORD = "admin1234";

    @Test
    public void test() {
        // Boolean values to test if exceptions will be caught
        boolean clientProtocolExceptionCaught = false;
        boolean ioExceptionCaught = false;

        // Create instagram object
        Instagram4j instagram4j = Instagram4j.builder().username(USERNAME).password(PASSWORD).build();
        instagram4j.setup();

        // Init login result
        InstagramLoginResult loginResult = null;

        // Try to login and catch all errors
        try {
            loginResult = instagram4j.login();
        } catch (ClientProtocolException e) {
            clientProtocolExceptionCaught = true;
            e.printStackTrace();
        } catch (IOException e) {
            ioExceptionCaught = true;
            e.printStackTrace();
        }

        // Make sure that no exceptions occurred
        assertFalse(clientProtocolExceptionCaught);
        assertFalse(ioExceptionCaught);

        // Make sure that login was successful
        assertNotNull(loginResult);
        LOGGER.info("loginResult=" + loginResult);

        // Extract logged in user
        InstagramLoggedUser loggedInUser = loginResult.getLogged_in_user();

        // Make sure that user was found
        assertNotNull(loggedInUser);
        LOGGER.info("loggedInUser=" + loggedInUser);

        // Make sure that we logged in with correct username
        assertEquals(USERNAME, loggedInUser.getUsername());
    }
}