package com.example.simple_template.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private SecretKey secretKey;

    @Value("${template.jwt.expire}")
    private long expire;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createToken(Long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId + "")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public int getUserId(String token) {
        Claims claims = getClaimByToken(token);
        if (claims != null) {
            return Integer.parseInt(claims.getSubject());
        }
        return 0;
    }
}
