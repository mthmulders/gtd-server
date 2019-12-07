package com.infosupport.training.reactjs.gtdserver.users;

import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.security.UserAuthenticationService;
import com.infosupport.training.reactjs.gtdserver.security.UserService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PublicUsersControllerTest {
    private static final String PASSWORD = ".`T*bu*{A~3xZ_E";
    private static final String USERNAME = "john.doe@example.com";

    private final UserAuthenticationService authentication = mock(UserAuthenticationService.class);
    private final UserService users = mock(UserService.class);

    private final PublicUsersController controller = new PublicUsersController(authentication, users);

    @Test
    public void register_shouldStoreUser() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        when(authentication.login(USERNAME, PASSWORD)).thenReturn(Optional.of(token));

        // Act
        controller.register(USERNAME, PASSWORD);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(users).save(userCaptor.capture());
        final User storedUser = userCaptor.getValue();
        assertThat(storedUser.getUsername(), is(USERNAME));
        assertThat(storedUser.getPassword(), is(PASSWORD));
    }

    @Test
    public void login_withValidCredentials_shouldGenerateToken() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        when(authentication.login(USERNAME, PASSWORD)).thenReturn(Optional.of(token));

        // Act
        final String result = controller.login(USERNAME, PASSWORD);

        // Assert
        assertThat(result, is(token));
    }

    @Test(expected = RuntimeException.class)
    public void login_withInvalidCredentials_shouldThrowException() {
        // Arrange
        final String token = UUID.randomUUID().toString();
        when(authentication.login(USERNAME, PASSWORD)).thenReturn(Optional.empty());

        // Act
        controller.login(USERNAME, PASSWORD);
    }
}