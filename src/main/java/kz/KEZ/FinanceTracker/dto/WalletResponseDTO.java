package kz.KEZ.FinanceTracker.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponseDTO {

    private Long id;
    private String name;
    private BigDecimal currentBalance;

}
