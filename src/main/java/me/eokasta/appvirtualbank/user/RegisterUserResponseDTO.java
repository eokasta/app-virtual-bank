package me.eokasta.appvirtualbank.user;

public record RegisterUserResponseDTO(
        Long id,
        String cpf,
        String fullName,
        String email,
        UserRole role
) {
}
