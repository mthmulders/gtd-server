package com.infosupport.training.reactjs.gtdserver.security;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Signals that a user has registered. {@link #getSource()} gives the {@link User} instance.
 */
@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    public UserRegisteredEvent(Object source) {
        super(source);
    }
}
