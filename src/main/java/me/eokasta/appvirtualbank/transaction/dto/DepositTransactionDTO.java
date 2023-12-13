package me.eokasta.appvirtualbank.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record DepositTransactionDTO(
        @NotBlank(message = "O CPF do remetente não pode ser vazio.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve estar no padrão 00000000000.")
        String cpfSender,
        @NotBlank(message = "O CPF do destinatário não pode ser vazio.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve estar no padrão 00000000000.")
        String cpfReceiver,
        @NotNull(message = "O valor não pode ser vazio.")
        BigDecimal value
) {
}
