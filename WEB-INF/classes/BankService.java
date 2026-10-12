// package banksysteam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankService {
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, Customer> customers = new HashMap<>();

    private static final AtomicLong customerIdCounter = new AtomicLong(1000);
    private static final AtomicLong accountNumberCounter = new AtomicLong(1000000000L);

    public Customer createCustomer(String name, String email, String phone) {
        String newId = "CUST-" + customerIdCounter.incrementAndGet();
        Customer customer = new Customer(newId, name, email, phone);
        customers.put(newId, customer);

        String sql = "INSERT INTO customers (customer_id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newId);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public Account openAccount(String customerId, double initialDeposit) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        String accountNumber = "ACC-" + accountNumberCounter.incrementAndGet();
        Account account = new Account(accountNumber, customerId, 0.0);

        customer.addAccount(account);
        accounts.put(accountNumber, account);

        // 1. Insert into accounts table
        String sql = "INSERT INTO accounts (account_number, customer_id, balance) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, customerId);
            stmt.setDouble(3, initialDeposit);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Insert initial transaction if deposit > 0
        if (initialDeposit > 0) {
            account.deposit(initialDeposit);
            Transaction tx = new Transaction(
                TransactionType.DEPOSIT,
                initialDeposit,
                account.getBalance(),
                "Initial opening deposit"
            );
            account.addTransaction(tx);
            saveTransactionToDB(accountNumber, "DEPOSIT", initialDeposit, account.getBalance(), "Initial opening deposit");
        }

        return account;
    }

    public void deposit(String accountNumber, double amount) {
        Account account = getAccountOrThrow(accountNumber);
        account.deposit(amount);
        account.addTransaction(new Transaction(
            TransactionType.DEPOSIT,
            amount,
            account.getBalance(),
            "Cash deposit"
        ));

        // Update account balance in DB
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Save transaction record to DB
        saveTransactionToDB(accountNumber, "DEPOSIT", amount, account.getBalance(), "Cash deposit");
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = getAccountOrThrow(accountNumber);
        account.withdraw(amount);
        account.addTransaction(new Transaction(
            TransactionType.WITHDRAWAL,
            amount,
            account.getBalance(),
            "Cash withdrawal"
        ));

        // Update account balance in DB
        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Save transaction record to DB
        saveTransactionToDB(accountNumber, "WITHDRAWAL", amount, account.getBalance(), "Cash withdrawal");
    }

    public void transfer(String fromAccNumber, String toAccNumber, double amount) {
        if (fromAccNumber.equals(toAccNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        Account fromAccount = getAccountOrThrow(fromAccNumber);
        Account toAccount = getAccountOrThrow(toAccNumber);

        fromAccount.withdraw(amount);
        fromAccount.addTransaction(new Transaction(
            TransactionType.WITHDRAWAL,
            amount,
            fromAccount.getBalance(),
            "Transfer sent to: " + toAccNumber
        ));

        toAccount.deposit(amount);
        toAccount.addTransaction(new Transaction(
            TransactionType.TRANSFER_IN,
            amount,
            toAccount.getBalance(),
            "Transfer received from: " + fromAccNumber
        ));

        // Update account balances in DB
        String deductSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        String creditSql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement deductStmt = conn.prepareStatement(deductSql);
                 PreparedStatement creditStmt = conn.prepareStatement(creditSql)) {
                
                deductStmt.setDouble(1, amount);
                deductStmt.setString(2, fromAccNumber);
                deductStmt.executeUpdate();

                creditStmt.setDouble(1, amount);
                creditStmt.setString(2, toAccNumber);
                creditStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Save transaction records for both accounts to DB
        saveTransactionToDB(fromAccNumber, "WITHDRAWAL", amount, fromAccount.getBalance(), "Transfer to: " + toAccNumber);
        saveTransactionToDB(toAccNumber, "TRANSFER_IN", amount, toAccount.getBalance(), "Transfer from: " + fromAccNumber);
    }

    public List<Transaction> getStatement(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);
        return account.getTransactions();
    }

    private Account getAccountOrThrow(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }

    // Helper method to insert transaction rows into the MySQL database
    private void saveTransactionToDB(String accountNumber, String type, double amount, double balanceAfter, String description) {
        String txId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
        String sql = "INSERT INTO transactions (transaction_id, account_number, type, amount, balance_after, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txId);
            stmt.setString(2, accountNumber);
            stmt.setString(3, type);
            stmt.setDouble(4, amount);
            stmt.setDouble(5, balanceAfter);
            stmt.setString(6, description);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}