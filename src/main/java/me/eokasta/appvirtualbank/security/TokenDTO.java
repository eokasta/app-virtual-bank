package me.eokasta.appvirtualbank.security;

import java.time.Instant;

public record TokenDTO(
        String token,
        String cpf,
        Instant expirationDate
) {

    public boolean isExpired() {
        return Instant.now().isAfter(expirationDate);
    }

}
