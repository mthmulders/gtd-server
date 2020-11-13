package com.infosupport.training.reactjs.gtdserver.security;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

        // Assert
        await().atMost(2, SECONDS).until(() -> service.verify(token).isEmpty());
    }
}