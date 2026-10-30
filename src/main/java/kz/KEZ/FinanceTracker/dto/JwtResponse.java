package kz.KEZ.FinanceTracker.dto;


import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String refreshToken;

    public JwtResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
