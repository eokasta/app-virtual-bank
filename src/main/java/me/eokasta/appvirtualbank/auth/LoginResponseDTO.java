package me.eokasta.appvirtualbank.auth;

import java.time.Instant;

public record LoginResponseDTO(String token, Instant expiresAt) {
}
