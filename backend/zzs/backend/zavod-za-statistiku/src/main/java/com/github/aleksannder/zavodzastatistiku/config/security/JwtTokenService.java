package com.github.aleksannder.zavodzastatistiku.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Service
public class JwtTokenService {

    @Value("${security.jwt.local-issuer}")
    private String localIssuer;

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.access-ttl-seconds}")
    private long accessTtl;

    @Value("${security.jwt.refresh-ttl-seconds}")
    private long refreshTtl;

    private Key key() { return Keys.hmacShaKeyFor(secret.getBytes()); }

    public String generateAccessToken(String email, Set<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(localIssuer)
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTtl)))
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS256, key())
                .compact();
    }

    public String generateRefreshToken(String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(localIssuer)
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refreshTtl)))
                .claim("typ", "refresh")
                .signWith(SignatureAlgorithm.HS256, key())
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token);
    }

    public boolean isRefresh(String token) {
        try { return "refresh".equals(parse(token).getBody().get("typ")); }
        catch (JwtException e) { return false; }
    }

    public long accessTtl() { return accessTtl; }
    public long refreshTtl() { return refreshTtl; }
}
