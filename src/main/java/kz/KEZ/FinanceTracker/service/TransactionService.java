package kz.KEZ.FinanceTracker.service;

import kz.KEZ.FinanceTracker.dto.CategoryInfo;
import kz.KEZ.FinanceTracker.dto.TransactionDTO;
import kz.KEZ.FinanceTracker.dto.TransactionResponseDTO;
import kz.KEZ.FinanceTracker.models.Category;
import kz.KEZ.FinanceTracker.models.Transaction;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.models.Wallet;
import kz.KEZ.FinanceTracker.repositories.TransactionRepository;
import kz.KEZ.FinanceTracker.repositories.UserRepository;

import kz.KEZ.FinanceTracker.repositories.WalletsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

    private final WalletsRepository walletRepository;

    @Transactional
    public TransactionResponseDTO createTransaction(User user, TransactionDTO transactionDTO) {

        Category category = categoryService.findById(transactionDTO.getCategory())
                .orElseThrow(() -> new RuntimeException("Категория не найдена с ID: " + transactionDTO.getCategory()));

        Wallet wallet = walletRepository.findById(transactionDTO.getWalletId())
                .orElseThrow(() -> new RuntimeException("Кошелек не найден с ID: " + transactionDTO.getWalletId()));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount().setScale(2, RoundingMode.HALF_UP));
        transaction.setType(transactionDTO.getType());
        transaction.setDate(LocalDateTime.now());

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setWallet(wallet);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToResponseDTO(savedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<TransactionResponseDTO> getUserTransactions(User user) {


        List<Transaction> transactions = transactionRepository.findByUser(user);

        return transactions.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();

        responseDTO.setId(transaction.getId());
        responseDTO.setAmount(transaction.getAmount());
        responseDTO.setType(transaction.getType());
        responseDTO.setDate(transaction.getDate());

        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setId(transaction.getCategory().getId());
        categoryInfo.setName(transaction.getCategory().getName());
        responseDTO.setCategory(categoryInfo);

        responseDTO.setWalletId(transaction.getWallet().getId());
        responseDTO.setWalletName(transaction.getWallet().getName());

        return responseDTO;
    }

    public BigDecimal getBalance(User user) {
        List<Wallet> wallets = walletRepository.findByUser(user);

        BigDecimal totalBalance = BigDecimal.ZERO;
        for (Wallet wallet : wallets) {
            totalBalance = totalBalance.add(calculateWalletBalance(wallet.getId()));
        }

        return totalBalance;
    }

    public BigDecimal calculateWalletBalance(Long walletId) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);

        return transactions.stream()
                .map(t -> {
                    if ("INCOME".equalsIgnoreCase(t.getType())) {
                        return t.getAmount();
                    } else {
                        return t.getAmount().negate();
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void recordInitialDeposit(Long userId, Long walletId, BigDecimal amount) {
        final Long INITIAL_DEPOSIT_CATEGORY_ID = 1L;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Кошелек не найден с ID: " + walletId));

        Optional<Category> initialDepositCategory = categoryService.findById(INITIAL_DEPOSIT_CATEGORY_ID);

        if (initialDepositCategory.isEmpty()) {
            System.err.println("WARN: System Category for Initial Deposit not found (ID=1). Skipping initial transaction.");
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setWallet(wallet);
        transaction.setCategory(initialDepositCategory.get());
        transaction.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
        transaction.setType("INCOME");
        transaction.setDate(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}
