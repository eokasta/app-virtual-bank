package me.eokasta.appvirtualbank.user;

import java.math.BigDecimal;
import java.time.Instant;

public record GetUserResponseDTO(
        Long id,
        String cpf,
        String fullName,
        String email,
        UserRole role,
        Instant createdAt,
        BigDecimal balance
) {
}
