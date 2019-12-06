package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JWTTokenServiceTest {
    private static final String ISSUER = "test";
    private static final int EXPIRATION_SEC = 1;
    private static final int CLOCK_SKEW_SEC = 0;
    private static final String SECRET = "test";

    private JWTTokenService service = new JWTTokenService(ISSUER, EXPIRATION_SEC, CLOCK_SKEW_SEC, SECRET);

    @Test
    public void expiring_shouldCreateValidToken() {
        // Arrange
        final Map<String, String> attributes = Collections.singletonMap("user", "john@doe.com");

        // Act
        final String token = service.expiring(attributes);

        // Assert
        final Map<String, String> result = service.verify(token);
        assertThat(result.containsKey("user"), is(true));
        assertThat(result.get("user"), is("john@doe.com"));
    }

    @Test
    public void expiring_shouldCreateTokenThatExpires() throws InterruptedException {
        // Arrange
        final Map<String, String> attributes = Collections.singletonMap("user", "john@doe.com");

        // Act
        final String token = service.expiring(attributes);
        Thread.sleep(1_100);

        // Assert
        final Map<String, String> result = service.verify(token);
        assertThat(result.isEmpty(), is(true));
    }
}