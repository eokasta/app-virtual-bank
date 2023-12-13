package me.eokasta.appvirtualbank.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.eokasta.appvirtualbank.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration.minutes}")
    private int expirationMinutes;

    public String generateToken(User user) {

        try {
            final Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getCpf())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error on generate token.", exception);
        }
    }

    public TokenDTO validateToken(String token) {
        try {
            final Algorithm algorithm = Algorithm.HMAC256(secret);
            final DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);

            return new TokenDTO(
                    token,
                    decodedJWT.getSubject(),
                    decodedJWT.getExpiresAtAsInstant()
            );
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error on validate token.", exception);
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusMinutes(expirationMinutes).toInstant(ZoneOffset.of("-3"));
    }

}
