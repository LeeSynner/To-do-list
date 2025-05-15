package com.example.to_do_list.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

public class JwtServiceTest {
    final String secret;

    final private JwtService jwtService;

    public JwtServiceTest() {
        secret = "12345678901234567890123456789012";
        jwtService = new JwtService(secret);
    }

    @ParameterizedTest
    @CsvSource({
            "testusername"
    })
    void testGenerateToken(String username) {
        String jwtToken = jwtService.generateToken(username);
        assertThat(jwtToken).isNotNull().isNotEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "testusername",
            "test2username"
    })
    void testValidateToken_success(String username) {
        String token = generateValidToken(username);
        boolean result = jwtService.validateToken(token);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "1234512345",
            "fakedtoken",
            ",",
            "''",
            "1",
            "a"
    })
    void testValidateToken_failed(String token) {
        boolean result = jwtService.validateToken(token);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "testusername",
            "test2username"
    })
    void testValidateToken_whenTokenIsExpired(String username) {
        String expiredToken = generateExpiredToken(username);
        boolean result = jwtService.validateToken(expiredToken);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "testusername",
            "test2username",
            "aaa",
            "111"
    })
    void testExtractUsername(String username) {
        String token = generateValidToken(username);
        String extractedUsername = jwtService.extractUsername(token);
        assertThat(extractedUsername).isEqualTo(username);
    }

    private String generateValidToken(String username) {
        return generateToken(username, new Date(System.currentTimeMillis() + 86400000));
    }

    private String generateExpiredToken(String username) {
        return generateToken(username, new Date(System.currentTimeMillis() - 1));
    }

    private String generateToken(String username, Date expiration) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
