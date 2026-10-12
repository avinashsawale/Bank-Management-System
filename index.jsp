<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title> Bank Management Portal</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body { background-color: #f4f6f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
    .navbar-bank { background: linear-gradient(135deg, #0d3b66, #0077b6); }
    .btn-bank { background-color: #0d3b66; color: #fff; font-weight: 500; }
    .btn-bank:hover { background-color: #082642; color: #fff; }
    .card-portal { border: none; border-radius: 0.75rem; box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
    .nav-pills .nav-link.active { background-color: #0d3b66; }
    .nav-pills .nav-link { color: #333; font-weight: 500; }
  </style>
</head>
<body>

  <!-- Top Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark navbar-bank shadow-sm">
    <div class="container">
      <a class="navbar-brand fw-bold d-flex align-items-center gap-2" href="index.jsp">
        <i class="bi bi-bank fs-3"></i>
        <span>Bank Management System</span>
      </a>
      <span class="navbar-text text-white small">
        <i class="bi bi-shield-check me-1"></i> Core Banking Engine
      </span>
    </div>
  </nav>

  <div class="container py-4">

    <!-- Notification Message Display -->
    <% 
      String msg = request.getParameter("msg");
      String success = request.getParameter("success");
      if (msg != null) { 
    %>
      <div class="alert <%= "true".equals(success) ? "alert-success" : "alert-danger" %> alert-dismissible fade show" role="alert">
        <strong><%= "true".equals(success) ? "Success!" : "Notice:" %></strong> <%= msg %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    <% } %>

    <div class="row g-4">
      <!-- Left Column: Operations Menu -->
      <div class="col-lg-4">
        <div class="card card-portal p-3 bg-white mb-4">
          <h6 class="text-uppercase text-muted fw-bold mb-3 small">Banking Operations</h6>
          <div class="nav flex-column nav-pills gap-2" id="v-pills-tab" role="tablist">
            <button class="nav-link active text-start" data-bs-toggle="pill" data-bs-target="#pill-customer">
              <i class="bi bi-person-plus me-2"></i> 1. Create Customer
            </button>
            <button class="nav-link text-start" data-bs-toggle="pill" data-bs-target="#pill-account">
              <i class="bi bi-wallet2 me-2"></i> 2. Open Account
            </button>
            <button class="nav-link text-start" data-bs-toggle="pill" data-bs-target="#pill-deposit">
              <i class="bi bi-arrow-down-circle me-2"></i> 3. Deposit Cash
            </button>
            <button class="nav-link text-start" data-bs-toggle="pill" data-bs-target="#pill-withdraw">
              <i class="bi bi-arrow-up-circle me-2"></i> 4. Withdraw Cash
            </button>
            <button class="nav-link text-start" data-bs-toggle="pill" data-bs-target="#pill-transfer">
              <i class="bi bi-arrow-left-right me-2"></i> 5. Fund Transfer
            </button>
            <button class="nav-link text-start" data-bs-toggle="pill" data-bs-target="#pill-statement">
              <i class="bi bi-file-earmark-text me-2"></i> 6. Account Statement
            </button>
          </div>
        </div>
      </div>

      <!-- Right Column: Interactive Forms -->
      <div class="col-lg-8">
        <div class="card card-portal p-4 bg-white">
          <div class="tab-content" id="v-pills-tabContent">

            <!-- 1. Create Customer Form -->
            <div class="tab-pane fade show active" id="pill-customer">
              <h4 class="fw-bold text-dark mb-1">Create Customer</h4>
              <p class="text-muted small mb-4">Register a new customer profile into the database.</p>
              <form action="bank" method="POST">
                <input type="hidden" name="action" value="createCustomer">
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Full Name</label>
                  <input type="text" name="name" class="form-control" placeholder="e.g. Ramesh Patil" required>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Email Address</label>
                  <input type="email" name="email" class="form-control" placeholder="e.g. ramesh@example.com" required>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Phone Number</label>
                  <input type="tel" name="phone" class="form-control" placeholder="e.g. 9876543210" required>
                </div>
                <button type="submit" class="btn btn-bank px-4">Register Customer</button>
              </form>
            </div>

            <!-- 2. Open Account Form -->
            <div class="tab-pane fade" id="pill-account">
              <h4 class="fw-bold text-dark mb-1">Open Bank Account</h4>
              <p class="text-muted small mb-4">Link a new bank account to an existing Customer ID.</p>
              <form action="bank" method="POST">
                <input type="hidden" name="action" value="openAccount">
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Customer ID</label>
                  <input type="text" name="customerId" class="form-control" placeholder="e.g. CUST-1001" required>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Initial Deposit (₹)</label>
                  <input type="number" name="initialDeposit" min="0" step="0.01" class="form-control" placeholder="0.00" required>
                </div>
                <button type="submit" class="btn btn-bank px-4">Create Account</button>
              </form>
            </div>

            <!-- 3. Deposit Form -->
            <div class="tab-pane fade" id="pill-deposit">
              <h4 class="fw-bold text-dark mb-1">Deposit Cash</h4>
              <p class="text-muted small mb-4">Credit funds directly into an account.</p>
              <form action="bank" method="POST">
                <input type="hidden" name="action" value="deposit">
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Account Number</label>
                  <input type="text" name="accountNumber" class="form-control" placeholder="e.g. ACC-1000000001" required>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Amount to Deposit (₹)</label>
                  <input type="number" name="amount" min="1" step="0.01" class="form-control" placeholder="Amount" required>
                </div>
                <button type="submit" class="btn btn-success px-4">Complete Deposit</button>
              </form>
            </div>

            <!-- 4. Withdraw Form -->
            <div class="tab-pane fade" id="pill-withdraw">
              <h4 class="fw-bold text-dark mb-1">Withdraw Cash</h4>
              <p class="text-muted small mb-4">Debit funds from an active account.</p>
              <form action="bank" method="POST">
                <input type="hidden" name="action" value="withdraw">
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Account Number</label>
                  <input type="text" name="accountNumber" class="form-control" placeholder="e.g. ACC-1000000001" required>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Amount to Withdraw (₹)</label>
                  <input type="number" name="amount" min="1" step="0.01" class="form-control" placeholder="Amount" required>
                </div>
                <button type="submit" class="btn btn-danger px-4">Complete Withdrawal</button>
              </form>
            </div>

            <!-- 5. Transfer Form -->
            <div class="tab-pane fade" id="pill-transfer">
              <h4 class="fw-bold text-dark mb-1">Fund Transfer</h4>
              <p class="text-muted small mb-4">Transfer money between two bank accounts.</p>
              <form action="bank" method="POST">
                <input type="hidden" name="action" value="transfer">
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Source Account Number (Debit)</label>
                  <input type="text" name="fromAccount" class="form-control" placeholder="e.g. ACC-1000000001" required>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Destination Account Number (Credit)</label>
                  <input type="text" name="toAccount" class="form-control" placeholder="e.g. ACC-1000000002" required>
                </div>
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Transfer Amount (₹)</label>
                  <input type="number" name="amount" min="1" step="0.01" class="form-control" placeholder="Amount" required>
                </div>
                <button type="submit" class="btn btn-primary px-4">Execute Transfer</button>
              </form>
            </div>

            <!-- 6. Account Statement Search -->
            <div class="tab-pane fade" id="pill-statement">
              <h4 class="fw-bold text-dark mb-1">Account Statement</h4>
              <p class="text-muted small mb-4">Look up transaction history for an account.</p>
              <form method="GET" action="statement.jsp">
                <div class="mb-3">
                  <label class="form-label fw-semibold small">Account Number</label>
                  <div class="input-group">
                    <input type="text" name="accountNumber" class="form-control" placeholder="e.g. ACC-1000000001" required>
                    <button class="btn btn-bank px-4" type="submit">View Statement</button>
                  </div>
                </div>
              </form>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>