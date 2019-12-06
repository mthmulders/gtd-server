package com.infosupport.training.reactjs.gtdserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class JWTTokenService implements Clock, TokenService {
    private static final CompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private final String issuer;
    private final int expirationSec;
    private final int clockSkewSec;
    private final String secretKey;

    public JWTTokenService(@Value("${jwt.issuer:gtd}") final String issuer,
                           @Value("${jwt.expiration-sec:86400}") final int expirationSec,
                           @Value("${jwt.clock-skew-sec:300}") final int clockSkewSec,
                           @Value("${jwt.secret:s3cr3t}") final String secret) {
        super();
        this.issuer = requireNonNull(issuer);
        this.expirationSec = expirationSec;
        this.clockSkewSec = clockSkewSec;
        this.secretKey = BASE64.encode(requireNonNull(secret));
    }

    @Override
    public String expiring(final Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    private String newToken(final Map<String, String> attributes, final int expiresInSec) {
        final ZonedDateTime now = ZonedDateTime.now();
        final Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now.toInstant()));

        if (expiresInSec > 0) {
            final ZonedDateTime expiresAt = now.plusSeconds(expiresInSec);
            claims.setExpiration(Date.from(expiresAt.toInstant()));
        }
        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    @Override
    public Map<String, String> verify(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        try {
            final Claims claims = toClaims.get();
            final Map<String, String> result = new HashMap<>();
            for (final Map.Entry<String, Object> e: claims.entrySet()) {
                result.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return result;
        } catch (final IllegalArgumentException | JwtException e) {
            log.error("Error parsing claims", e);
            return Collections.emptyMap();
        }
    }

    @Override
    public Date now() {
        return Date.from(ZonedDateTime.now().toInstant());
    }
}
