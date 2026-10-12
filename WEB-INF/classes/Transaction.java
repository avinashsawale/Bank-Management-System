

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String transactionId;
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final double amount;
    private final double balanceAfter;
    private final String description;

    // Full constructor
    public Transaction(TransactionType type, double amount, double balanceAfter, String description) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero.");
        }

        // Generate unique transaction ID and record the current timestamp automatically
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + type + " - Amount: $" + amount + 
               " | Balance After: $" + balanceAfter + " | Note: " + description;
    }
}
