package com.pdv.lalapan.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pdv.lalapan.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        return JWT.create()
                .withIssuer("lalapan")
                .withSubject(usuario.getUsername())
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validar(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("lalapan")
                .build()
                .verify(token)
                .getSubject();
    }
}
