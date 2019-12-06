package com.infosupport.training.reactjs.gtdserver.security;

import java.util.Optional;

public interface UserCrudService {
    void save(User user);

    Optional<User> findByUsername(String username);
}