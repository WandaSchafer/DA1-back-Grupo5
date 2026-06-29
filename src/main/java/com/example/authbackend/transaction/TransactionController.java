package com.example.authbackend.transaction;

import com.example.authbackend.security.user.CustomUserDetails;
import com.example.authbackend.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping({"", "/me"})
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        List<TransactionResponse> transactions = transactionService.getTransactionsForUser(currentUser);
        return ResponseEntity.ok(transactions);
    }
}
