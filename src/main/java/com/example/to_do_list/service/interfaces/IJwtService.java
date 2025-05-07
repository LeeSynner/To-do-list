package com.example.to_do_list.service.interfaces;

import javax.crypto.SecretKey;

public interface IJwtService {
    String generateToken(String username);

    boolean validateToken(String token);

    String extractUsername(String token);
}
