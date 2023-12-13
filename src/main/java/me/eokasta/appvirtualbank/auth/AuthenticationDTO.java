package me.eokasta.appvirtualbank.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthenticationDTO(
        @NotBlank(message = "O CPF não pode ser vazio.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve estar no padrão 00000000000.")
        String cpf,
        @NotBlank(message = "A senha não pode ser vazia.")
        String password
) {
}
