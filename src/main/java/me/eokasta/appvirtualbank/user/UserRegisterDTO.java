package me.eokasta.appvirtualbank.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

public record UserRegisterDTO(
        @NotBlank(message = "O CPF não pode ser vazio.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve estar no padrão 00000000000.")
        String cpf,
        @NotBlank(message = "A senha não pode ser vazia.")
        String password,
        @NotBlank(message = "O nome não pode ser vazio.")
        String fullName,
        @NotBlank(message = "O email não pode ser vazio.")
        String email,
        @NotNull(message = "O cargo não pode ser vazio.")
        UserRole role
) {

    public User toUser() {
        final String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        return new User(null, cpf, encryptedPassword, fullName, email, BigDecimal.ZERO, role);
    }

}
