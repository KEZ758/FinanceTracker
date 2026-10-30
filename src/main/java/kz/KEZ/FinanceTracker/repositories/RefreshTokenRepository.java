package kz.KEZ.FinanceTracker.repositories;

import kz.KEZ.FinanceTracker.models.RefreshToken;
import kz.KEZ.FinanceTracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);

    void deleteByToken(String token);
}
