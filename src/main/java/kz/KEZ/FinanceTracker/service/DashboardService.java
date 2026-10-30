package kz.KEZ.FinanceTracker.service;


import kz.KEZ.FinanceTracker.dto.DashboardDTO;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.models.Wallet;
import kz.KEZ.FinanceTracker.repositories.TransactionRepository;
import kz.KEZ.FinanceTracker.repositories.UserRepository;
import kz.KEZ.FinanceTracker.repositories.WalletsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final WalletsRepository walletsRepository;
    private final TransactionRepository transactionRepository;

    public DashboardDTO getDashboard(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        LocalDate end = today.withDayOfMonth(today.lengthOfMonth());

        List<Wallet> wallets = walletsRepository.findByUserId(user.getId());

        BigDecimal totalBalance = wallets.stream()
                .map(Wallet::get)

    }
}
