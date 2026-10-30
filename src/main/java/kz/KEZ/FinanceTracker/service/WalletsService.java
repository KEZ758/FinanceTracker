package kz.KEZ.FinanceTracker.service;


import kz.KEZ.FinanceTracker.dto.WalletCreateDTO;
import kz.KEZ.FinanceTracker.dto.WalletResponseDTO;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.models.Wallet;
import kz.KEZ.FinanceTracker.repositories.UserRepository;
import kz.KEZ.FinanceTracker.repositories.WalletsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletsService {

    private final WalletsRepository walletsRepository;
    private final UserRepository userRepository;

    private final TransactionService transactionService;

    private WalletResponseDTO mapToResponseDTO(Wallet wallet) {
        WalletResponseDTO dto = new WalletResponseDTO();
        dto.setId(wallet.getId());
        dto.setName(wallet.getName());

        BigDecimal balance = transactionService.calculateWalletBalance(wallet.getId());
        dto.setCurrentBalance(balance);

        return dto;
    }

    public WalletResponseDTO createWallet(Long userId, WalletCreateDTO createDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setName(createDTO.getName());

        Wallet savedWallet = walletsRepository.save(wallet);

        if (createDTO.getInitialDeposit() != null && createDTO.getInitialDeposit().compareTo(BigDecimal.ZERO) > 0) {
            transactionService.recordInitialDeposit(
                    userId,
                    savedWallet.getId(),
                    createDTO.getInitialDeposit()
            );
        }

        return mapToResponseDTO(savedWallet);
    }

    public List<WalletResponseDTO> getUserWallets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Wallet> wallets = walletsRepository.findByUser(user);

        return wallets.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Wallet findById(Long walletId) {
        return walletsRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found with id: " + walletId));
    }
}
