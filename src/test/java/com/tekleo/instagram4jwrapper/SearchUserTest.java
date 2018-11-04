package com.tekleo.instagram4jwrapper;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoggedUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SearchUserTest {
    private static final Logger LOGGER = Logger.getLogger(SearchUserTest.class);
    private static final String USERNAME = "jpleorx1234";
    private static final String PASSWORD = "admin1234";
    private static final String SEARCHED_USERNAME = "jpleorx";

    @Test
    public void test() {
        // Login
        Instagram4j instagram4j = this.login();

        // Boolean values to test if exceptions will be caught
        boolean clientProtocolExceptionCaught = false;
        boolean ioExceptionCaught = false;

        // Initialize search result
        InstagramSearchUsernameResult searchUsernameResult = null;

        // Try to find user and catch all errors
        try {
            searchUsernameResult = instagram4j.sendRequest(new InstagramSearchUsernameRequest(SEARCHED_USERNAME));
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

        // Make sure that search was successful
        assertNotNull(searchUsernameResult);
        LOGGER.info("searchUsernameResult=" + searchUsernameResult);

        // Extract found user
        InstagramUser foundUser = searchUsernameResult.getUser();

        // Make sure that user was found
        assertNotNull(foundUser);
        LOGGER.info("foundUser=" + foundUser);

        // Make sure that we found a user with correct username
        assertEquals(SEARCHED_USERNAME, foundUser.getUsername());
    }

    private Instagram4j login() {
        // Create instagram object
        Instagram4j instagram4j = Instagram4j.builder().username(USERNAME).password(PASSWORD).build();
        instagram4j.setup();

        // Try to login
        try {
            // Perform login
            InstagramLoginResult loginResult = instagram4j.login();
            LOGGER.info("loginResult=" + loginResult);

            // Extract logged in user
            InstagramLoggedUser loggedInUser = loginResult.getLogged_in_user();
            LOGGER.info("loggedInUser=" + loggedInUser);

            return instagram4j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}