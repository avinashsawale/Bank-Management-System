<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, banksysteam.DBConnection" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Account Statement</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
  <div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h3>Transaction Statement for Account: <span class="text-primary"><%= request.getParameter("accountNumber") %></span></h3>
      <a href="index.jsp" class="btn btn-outline-secondary">← Back to Dashboard</a>
    </div>

    <div class="card shadow-sm border-0">
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-dark">
            <tr>
              <th>Txn ID</th>
              <th>Type</th>
              <th>Amount</th>
              <th>Balance After</th>
              <th>Description</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            <%
              String acc = request.getParameter("accountNumber");
              if (acc != null && !acc.trim().isEmpty()) {
                String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY created_at DESC";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, acc);
                  try (ResultSet rs = stmt.executeQuery()) {
                    boolean found = false;
                    while (rs.next()) {
                      found = true;
            %>
                      <tr>
                        <td><%= rs.getString("transaction_id") %></td>
                        <td><span class="badge bg-secondary"><%= rs.getString("type") %></span></td>
                        <td class="fw-bold">₹ <%= rs.getDouble("amount") %></td>
                        <td>₹ <%= rs.getDouble("balance_after") %></td>
                        <td><%= rs.getString("description") %></td>
                        <td class="small text-muted"><%= rs.getTimestamp("created_at") %></td>
                      </tr>
            <%
                    }
                    if (!found) {
            %>
                      <tr><td colspan="6" class="text-center py-4 text-muted">No transactions found for this account.</td></tr>
            <%
                    }
                  }
                } catch (Exception e) {
            %>
                  <tr><td colspan="6" class="text-danger py-4">Database Error: <%= e.getMessage() %></td></tr>
            <%
                }
              }
            %>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</body>
</html>