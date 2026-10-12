

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService bank = new BankService();

        while (true) {
            System.out.println("\n--- Bank Management System ---");
            System.out.println("1. Create Customer");
            System.out.println("2. Open Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Statement");
            System.out.println("7. Exit");
            System.out.print("Enter Your Choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            try {
                switch (choice) {
                case 1:
                    System.out.print("Enter Customer Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Customer Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Customer Phone: ");
                    String phone = scanner.nextLine();

                    Customer customer = bank.createCustomer(name, email, phone);
                    System.out.println("Customer created successfully!");
                    System.out.println("Assigned Customer ID: " + customer.getCustomerId());
                    break;

                    case 2:
                        System.out.print("Enter Customer ID: ");
                        String custId = scanner.nextLine();
                        System.out.print("Enter Initial Deposit Amount: ");
                        double initialDeposit = Double.parseDouble(scanner.nextLine());

                        Account account = bank.openAccount(custId, initialDeposit);
                        System.out.println("Account created successfully!");
                        System.out.println("Assigned Account Number: " + account.getAccountNumber());
                        System.out.println("Current Balance: $" + account.getBalance());
                        break;

                    case 3:
                        System.out.print("Enter Account Number: ");
                        String depAcc = scanner.nextLine();
                        System.out.print("Enter Amount to Deposit: ");
                        double depAmount = Double.parseDouble(scanner.nextLine());

                        bank.deposit(depAcc, depAmount);
                        System.out.println("Deposit successful!");
                        break;

                    case 4:
                        System.out.print("Enter Account Number: ");
                        String withAcc = scanner.nextLine();
                        System.out.print("Enter Amount to Withdraw: ");
                        double withAmount = Double.parseDouble(scanner.nextLine());

                        bank.withdraw(withAcc, withAmount);
                        System.out.println("Withdrawal successful!");
                        break;

                    case 5:
                        System.out.print("Enter Source Account Number: ");
                        String fromAcc = scanner.nextLine();
                        System.out.print("Enter Destination Account Number: ");
                        String toAcc = scanner.nextLine();
                        System.out.print("Enter Amount to Transfer: ");
                        double transferAmount = Double.parseDouble(scanner.nextLine());

                        bank.transfer(fromAcc, toAcc, transferAmount);
                        System.out.println("Transfer successful!");
                        break;

                    case 6:
                        System.out.print("Enter Account Number: ");
                        String stmtAcc = scanner.nextLine();

                        List<Transaction> statement = bank.getStatement(stmtAcc);
                        System.out.println("\n--- Transaction Statement for " + stmtAcc + " ---");
                        if (statement.isEmpty()) {
                            System.out.println("No transactions found.");
                        } else {
                            for (Transaction tx : statement) {
                                System.out.println(tx);
                            }
                        }
                        break;

                    case 7:
                        System.out.println("Exiting application. Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option. Please choose between 1 and 7.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}