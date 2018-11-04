package com.tekleo.instagram4jwrapper;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GetUsersFeedTest {
    private static final Logger LOGGER = Logger.getLogger(GetUsersFeedTest.class);
    private static final String USERNAME = "jpleorx1234";
    private static final String PASSWORD = "admin1234";
    private static final String SEARCHED_USERNAME = "jpleorx";

    @Test
    public void test() {
        // Login
        Instagram4j instagram4j = this.login();

        // Find user
        InstagramUser user = findUser(instagram4j, SEARCHED_USERNAME);

        // Boolean values to test if exceptions will be caught
        boolean clientProtocolExceptionCaught = false;
        boolean ioExceptionCaught = false;

        // Initialize search result
        InstagramFeedResult feedResult = null;

        // Try to find user and catch all errors
        try {
            feedResult = instagram4j.sendRequest(new InstagramUserFeedRequest(user.getPk()));
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
        assertNotNull(feedResult);
        LOGGER.info("feedResult=" + feedResult);

        // Extract feed items
        List<InstagramFeedItem> feedItems = feedResult.getItems();

        // Make sure that user was found
        assertNotNull(feedItems);
        assertFalse(feedItems.isEmpty());
        LOGGER.info("feedItems=" + feedItems);

        // Extract one feed items
        InstagramFeedItem lastFeedItem = feedItems.get(0);
        LOGGER.info("lastFeedItem=" + lastFeedItem);

        // Extract its caption
        String caption = (String) lastFeedItem.getCaption().get(CaptionFields.TEXT);
        LOGGER.info("caption=" + caption);
    }

    private static Instagram4j login() {
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

    private static InstagramUser findUser(Instagram4j instagram4j, String username) {
        // Try to find user and catch all errors
        try {
            InstagramSearchUsernameResult searchUsernameResult = instagram4j.sendRequest(new InstagramSearchUsernameRequest(username));
            LOGGER.info("searchUsernameResult=" + searchUsernameResult);

            InstagramUser foundUser = searchUsernameResult.getUser();
            LOGGER.info("foundUser=" + foundUser);

            return foundUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
