package me.eokasta.appvirtualbank.transaction;

import me.eokasta.appvirtualbank.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
