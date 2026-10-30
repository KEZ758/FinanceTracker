package kz.KEZ.FinanceTracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardDTO {
    BigDecimal totalBalance;
    BigDecimal monthExpenses;
    List<WalletResponseDTO> wallets;
    List<TransactionDTO> recentTransactions;
}

