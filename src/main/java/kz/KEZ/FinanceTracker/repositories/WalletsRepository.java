package kz.KEZ.FinanceTracker.repositories;

import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletsRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByUser(User user);
    List<Wallet> findByUserId(Long id);

    List<Wallet> findByIdAndUser(Long id, User user);
}
