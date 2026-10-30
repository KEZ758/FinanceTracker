package kz.KEZ.FinanceTracker.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {

    private Long id;
    private BigDecimal amount;
    private Long walletId;
    private String type;
    private long category;
}
