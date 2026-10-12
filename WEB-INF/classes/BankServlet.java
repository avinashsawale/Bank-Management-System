// package banksysteam;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



@WebServlet("/bank")
public class BankServlet extends HttpServlet {
    private static final long serialVersionUID = 1L; // <-- ADD THIS LINE

    private final BankService bankService = new BankService();
    // ... rest of your code

// @WebServlet("/bank")
// public class BankServlet extends HttpServlet {
//     private final BankService bankService = new BankService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String message = "";
        boolean isSuccess = true;

        try {
            if ("createCustomer".equals(action)) {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                Customer customer = bankService.createCustomer(name, email, phone);
                message = "Customer created successfully! Assigned ID: " + customer.getCustomerId();

            } else if ("openAccount".equals(action)) {
                String customerId = request.getParameter("customerId");
                double initialDeposit = Double.parseDouble(request.getParameter("initialDeposit"));
                Account account = bankService.openAccount(customerId, initialDeposit);
                message = "Account opened! Account No: " + account.getAccountNumber() + ", Balance: ₹" + account.getBalance();

            } else if ("deposit".equals(action)) {
                String accNo = request.getParameter("accountNumber");
                double amount = Double.parseDouble(request.getParameter("amount"));
                bankService.deposit(accNo, amount);
                message = "Successfully deposited ₹" + amount + " to " + accNo;

            } else if ("withdraw".equals(action)) {
                String accNo = request.getParameter("accountNumber");
                double amount = Double.parseDouble(request.getParameter("amount"));
                bankService.withdraw(accNo, amount);
                message = "Successfully withdrew ₹" + amount + " from " + accNo;

            } else if ("transfer".equals(action)) {
                String fromAcc = request.getParameter("fromAccount");
                String toAcc = request.getParameter("toAccount");
                double amount = Double.parseDouble(request.getParameter("amount"));
                bankService.transfer(fromAcc, toAcc, amount);
                message = "Transferred ₹" + amount + " from " + fromAcc + " to " + toAcc;
            }
        } catch (Exception e) {
            isSuccess = false;
            message = "Error: " + e.getMessage();
        }

        // Redirect back to dashboard with status
        response.sendRedirect("index.jsp?msg=" + java.net.URLEncoder.encode(message, "UTF-8") 
                              + "&success=" + isSuccess);
    }
}
