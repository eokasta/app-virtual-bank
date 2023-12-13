package me.eokasta.appvirtualbank.auth;

import jakarta.validation.Valid;
import me.eokasta.appvirtualbank.security.TokenService;
import me.eokasta.appvirtualbank.user.User;
import me.eokasta.appvirtualbank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserRepository repository,
            TokenService tokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.cpf(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        final Instant expiresAt = tokenService.validateToken(token).expirationDate();

        return ResponseEntity.ok(new LoginResponseDTO(token, expiresAt));
    }

}
