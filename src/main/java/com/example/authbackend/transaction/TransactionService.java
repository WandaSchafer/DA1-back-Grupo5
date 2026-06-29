package com.example.authbackend.transaction;

import com.example.authbackend.user.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> getTransactionsForUser(User user) {
        return transactionRepository.findByUserId(user.getId()).stream()
                .map(t -> new TransactionResponse(t.getId(), t.getAmount(), t.getCreatedAt(), t.getMaskedCard(), t.getStatus()))
                .collect(Collectors.toList());
    }
}
