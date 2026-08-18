package tech.skullprogrammer.bguard.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JWTService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JWTService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") Long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserDetails user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities())
                .issuedAt(new Date(now))
                .expiration(new Date(now + this.expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        Claims claims = extractClaims(token);
        return claims != null;
    }

    private Claims extractClaims(String token) {
        try {
            JwtParser jwtParser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            log.error(e.getMessage());
//            throw new SkullException(e.getMessage(), SkullException.ErrorType.JWT_ERROR);
            return null;
        }
    }

    public List<String> extractRoles(String token) {
        Collection c =  extractClaims(token).get("roles", Collection.class);
        return c.stream().map(g->((Map)g).get("authority")).toList();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

}
