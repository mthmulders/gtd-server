package com.infosupport.training.reactjs.gtdserver.security;

import java.util.Map;

public interface TokenService {
    String expiring(final Map<String, String> attributes);

    Map<String, String> verify(final String token);
}
