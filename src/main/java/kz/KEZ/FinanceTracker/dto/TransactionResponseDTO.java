package kz.KEZ.FinanceTracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String type;
    private CategoryInfo category;
    private LocalDateTime date;
    private Long walletId;
    private String walletName;
}

