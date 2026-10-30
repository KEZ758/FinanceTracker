package kz.KEZ.FinanceTracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public record DashboardDTO(
        BigDecimal totalBalance,
        BigDecimal monthExpenses,
        List<WalletResponseDTO> wallets,
        List<TransactionResponseDTO> recentTransactions
) {}

