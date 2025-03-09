package com.vakantes.Vakantes.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vakantes.Vakantes.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Usuario usuario) {
        SecretKey key = getKeyBySecret();
        return Jwts.builder().subject(usuario.getLogin())
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(key)
                .compact();
    }

    private SecretKey getKeyBySecret() {
        return Keys.hmacShaKeyFor(this.secret.getBytes());
    }


    public String validateToken(String token) {
        Claims claims = getClaims(token);
        try {
            if (claims != null) {
                return claims.getSubject();
            }

        } catch (JWTVerificationException exception) {
            return "";
        }
        return "";
    }


    private Claims getClaims(String token) {
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
