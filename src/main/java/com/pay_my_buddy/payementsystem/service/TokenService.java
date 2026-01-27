package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;


@Component
public class TokenService {
    @Autowired
    private UserRepository userRepository;
    @Value("${jwt.secret}")
    private String jwtSecret;
    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public SecretKey getSigningKey() {
        String secretKey = jwtSecret;
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public String generateToken(String identifier){
        SecretKey key = getSigningKey();
        String jwt = Jwts.builder()
                .subject(identifier)
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis() + 604800000L)) // 7 days
                .signWith(key)
                .compact();
        return jwt;
    }
    public String extractEMail(String token) {
        SecretKey key = getSigningKey();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean validateToken(String token, String user) {
        try {
            String email = extractEMail(token);
            return userRepository.findByEmail(email)!=null;

        } catch (Exception e) {
            return false;
        }



    }
}
