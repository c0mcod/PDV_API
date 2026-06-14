package com.pdv.lalapan.controllers;

import com.pdv.lalapan.dto.auth.LoginDTO;
import com.pdv.lalapan.entities.Usuario;
import com.pdv.lalapan.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;

    private final TokenService tokenService;

    public AuthController(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.nome(), dto.password());
        var auth = authManager.authenticate(authToken);

        String token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }
}
