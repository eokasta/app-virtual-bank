package me.eokasta.appvirtualbank.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "old_balance")
    private BigDecimal oldBalance;
    @Column(name = "new_balance")
    private BigDecimal newBalance;

    @PrePersist
    public void prePersist() {
        if (this.transactionDate == null)
            this.transactionDate = Instant.now();
    }

}
