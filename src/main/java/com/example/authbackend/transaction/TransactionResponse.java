package com.example.authbackend.transaction;

import java.time.LocalDateTime;

public class TransactionResponse {
    private Long id;
    private Double amount;
    private LocalDateTime createdAt;
    private String maskedCard;
    private TransactionStatus status;

    public TransactionResponse(Long id, Double amount, LocalDateTime createdAt, String maskedCard, TransactionStatus status) {
        this.id = id;
        this.amount = amount;
        this.createdAt = createdAt;
        this.maskedCard = maskedCard;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getMaskedCard() { return maskedCard; }
    public void setMaskedCard(String maskedCard) { this.maskedCard = maskedCard; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
}
