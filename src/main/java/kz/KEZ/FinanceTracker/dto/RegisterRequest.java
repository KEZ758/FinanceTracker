package kz.KEZ.FinanceTracker.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String name;
    private String password;

}
