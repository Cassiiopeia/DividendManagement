package com.springboot.dividendmanagement.security;

import org.springframework.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final int TOKEN_EXPIRED_TIME = 1000 * 60 * 60; // 1 hour
    private static final String KEY_ROLES = "roles";

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public String generateToken(String username, List<String> roles) {
        Claims claims = (Claims) Jwts.claims().subject(username);
        claims.put(KEY_ROLES, roles);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRED_TIME);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 알고리즘 암호화키
                .compact();
    }

    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
