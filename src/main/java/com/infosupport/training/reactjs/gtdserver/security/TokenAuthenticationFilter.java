package com.infosupport.training.reactjs.gtdserver.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.util.Optional.ofNullable;

/**
 * The TokenAuthenticationFilter is responsible of extracting the authentication token from the request headers.
 * It takes the Authorization header value and attempts to extract the token from it.
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    public TokenAuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        final String token = ofNullable(request.getHeader(AUTHORIZATION))
                .map(TokenAuthenticationFilter::removeBearer)
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));

        final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private static String removeBearer(final String input) {
        if (input.startsWith(BEARER)) {
            return input.substring(BEARER.length());
        }
        return input;
    }
}
