package com.infosupport.training.reactjs.gtdserver.users;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(final String message) {
        super(message);
    }
}
