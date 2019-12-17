package com.infosupport.training.reactjs.gtdserver.security;

import java.util.Optional;

public interface UserService {
    void create(User user);

    Optional<User> findByUsername(String username);
}