//package banksysteam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    // Fields matching the UML diagram
    private String accountNumber;
    private String customerId;
    private double balance;
    private List<Transaction> transactions;

    // Default constructor
    public Account() {
        this.transactions = new ArrayList<>();
    }

    // Parameterized constructor
    public Account(String accountNumber, String customerId, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // Business Methods
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > this.balance) {
            throw new IllegalStateException("Insufficient funds.");
        }
        this.balance -= amount;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }
        this.transactions.add(transaction);
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}
