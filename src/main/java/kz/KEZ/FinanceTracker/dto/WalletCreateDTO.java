package kz.KEZ.FinanceTracker.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletCreateDTO {
    private String name;
    private BigDecimal initialDeposit;
}
