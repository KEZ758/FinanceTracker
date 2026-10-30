package kz.KEZ.FinanceTracker.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
