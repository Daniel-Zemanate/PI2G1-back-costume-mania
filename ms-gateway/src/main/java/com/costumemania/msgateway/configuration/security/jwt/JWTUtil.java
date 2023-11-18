package com.costumemania.msgateway.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    public JWTUtil(Key key) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public boolean isExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }
}
