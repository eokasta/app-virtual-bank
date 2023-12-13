package me.eokasta.appvirtualbank.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.time.Instant;

@Table(name = "bank_transactions")
@Entity(name = "transactions")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_id")
    private Long accountId;
    private BigDecimal amount;
    @Column(name = "transaction_date")

    private Instant transactionDate;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @PrePersist
    public void prePersist() {
        if (this.transactionDate == null)
            this.transactionDate = Instant.now();
    }

}
