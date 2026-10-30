package kz.KEZ.FinanceTracker.repositories;

import kz.KEZ.FinanceTracker.dto.TransactionDTO;
import kz.KEZ.FinanceTracker.models.Transaction;
import kz.KEZ.FinanceTracker.models.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    List<Transaction> findByWalletId(Long walletId);

    @Query("""
    select coalesce(sum(t.amount), 0) from Transaction t
    where t.user = :user and t.type = 'EXPENSE'
      and t.date >= :from and t.date < :to
    """)
    BigDecimal sumExpenses(@Param("user") User user,
                           @Param("from") LocalDateTime from,
                           @Param("to") LocalDateTime to);

}

