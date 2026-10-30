package kz.KEZ.FinanceTracker.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.KEZ.FinanceTracker.dto.TransactionDTO;
import kz.KEZ.FinanceTracker.dto.TransactionResponseDTO;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.repositories.UserRepository;
import kz.KEZ.FinanceTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Транзакции", description = "Методы для работы с доходами и расходами")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @Operation(summary = "Создание транзакции")
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionDTO transactionDTO) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(transactionService.createTransaction(user, transactionDTO));
    }

    @Operation(
            summary = "Получить все транзакции пользователя",
            description = "Возвращает список всех финансовых операций пользователя"
    )
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getUserTransactions() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found from token email"));

        List<TransactionResponseDTO> transactions = transactionService.getUserTransactions(user);

        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Удаление транзакции")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @Operation(summary = "Получение баланса по всем счетам")
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found from token email"));
        return ResponseEntity.ok(transactionService.getBalance(user));
    }
}
