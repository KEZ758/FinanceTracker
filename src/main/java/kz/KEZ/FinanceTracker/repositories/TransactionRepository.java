package kz.KEZ.FinanceTracker.repositories;

import kz.KEZ.FinanceTracker.dto.TransactionDTO;
import kz.KEZ.FinanceTracker.models.Transaction;
import kz.KEZ.FinanceTracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    List<Transaction> findByWalletId(Long walletId);

    @Query("""
    select sum(-t.amount) from Transaction t
    where t.user.id = :userId and t.amount < 0
      and t.date between :from and :to
    """)
    BigDecimal sumExpenses(@Param("userId") Long userId,
                           @Param("from") LocalDate from,
                           @Param("to") LocalDate to);

}
