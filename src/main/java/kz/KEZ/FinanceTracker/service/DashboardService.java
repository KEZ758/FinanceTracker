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
    private final TransactionService transactionService;
    private final WalletsService walletsService;

    public DashboardDTO build(User user) {
        return new DashboardDTO(
                transactionService.getBalance(user),
                transactionService.getMonthExpenses(user),
                walletsService.getUserWallets(user.getId()),
                transactionService.getRecent(user, 10));
    }
}
