package kz.KEZ.FinanceTracker.controllers;


import kz.KEZ.FinanceTracker.dto.WalletCreateDTO;
import kz.KEZ.FinanceTracker.dto.WalletResponseDTO;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.repositories.UserRepository;
import kz.KEZ.FinanceTracker.service.WalletsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletsController {
    private final WalletsService walletsService;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found from token email"));
    }

    @GetMapping
    public ResponseEntity<List<WalletResponseDTO>> getUserWallets() {
        User user = getAuthenticatedUser();
        List<WalletResponseDTO> wallets = walletsService.getUserWallets(user.getId());
        return ResponseEntity.ok(wallets);
    }

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody WalletCreateDTO createDTO) {
        User user = getAuthenticatedUser();
        WalletResponseDTO newWallet = walletsService.createWallet(user.getId(), createDTO);
        return ResponseEntity.ok(newWallet);
    }
}
