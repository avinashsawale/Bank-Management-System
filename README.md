Bank Management System (BMS) - Web Application
A full-stack core banking web application developed using Java Servlets (Jakarta EE), JSP, JDBC, and MySQL, deployed on Apache Tomcat 10. 
The application provides an interactive web dashboard to manage customer profiles, bank accounts, cash deposits, withdrawals, fund transfers, and transaction history.

📌 Features
Customer Registration: Create customer profiles with auto-generated unique IDs (CUST-XXXX).

Account Management: Open bank accounts linked to customer IDs with an optional opening deposit.

Deposit & Withdrawal: Process real-time cash credit and debit operations.

Fund Transfer: Transfer funds atomically between accounts with balance validation.

Account Statement: View full transaction history (Type, Amount, Balance After, Timestamp, and Remarks).

Persistent Storage: All banking transactions and profiles persist in a MySQL database.

🛠️ Technology Stack & Prerequisites
Java Development Kit (JDK): Version 17 or higher

Web / Application Server: Apache Tomcat 10.x (Jakarta EE 9/10 compatible)

Database: MySQL 8.0+

Development Tool: Visual Studio Code (or Eclipse)

Dependencies (JARs):

jakarta.servlet-api-5.0.0.jar

mysql-connector-j-8.0.33.jar

🗄️ Database Setup (MySQL)
Open MySQL Command Line Client or MySQL Workbench.

Run the following script to create the database schema:

SQL
CREATE DATABASE IF NOT EXISTS bankdb;
USE bankdb;

-- 1. Customers Table
CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

-- 2. Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0.0,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- 3. Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount DOUBLE NOT NULL,
    balance_after DOUBLE NOT NULL,
    description VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
Ensure credentials in DBConnection.java match your MySQL setup:

Java
private static final String URL = "jdbc:mysql://localhost:3306/bankdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "YOUR_PASSWORD";
📁 Project Directory Structure
Plaintext
BMS/
├── lib/
│   ├── jakarta.servlet-api-5.0.0.jar
│   └── mysql-connector-j-8.0.33.jar
├── webapp/
│   ├── index.jsp
│   ├── statement.jsp
│   └── WEB-INF/
│       └── web.xml
├── Account.java
├── BankService.java
├── BankServlet.java
├── Customer.java
├── DBConnection.java
├── Main.java
├── Transaction.java
└── TransactionType.java
🚀 How to Build and Run Step-by-Step
Step 1: Copy MySQL Driver to Tomcat Library
Tomcat requires the MySQL JDBC driver in its shared library classpath:

Locate your downloaded mysql-connector-j-8.0.33.jar.

Copy it into your Tomcat installation directory:

Plaintext
<TOMCAT_DIR>\lib\
Step 2: Deploy Project to Tomcat
In your project directory or via PowerShell:

Create the application folder inside Tomcat's webapps:

PowerShell
$dest = "<TOMCAT_DIR>\webapps\BMS"
New-Item -ItemType Directory -Path "$dest\WEB-INF\classes" -Force
Copy the JSP files directly under webapps\BMS\:

PowerShell
Copy-Item "webapp\*.jsp" -Destination $dest -Force
Copy the web.xml deployment descriptor:

PowerShell
Copy-Item "webapp\WEB-INF\web.xml" -Destination "$dest\WEB-INF\" -Force
Compile all Java source files directly into Tomcat's WEB-INF/classes folder:

PowerShell
javac -cp "lib/*" -d "$dest\WEB-INF\classes" *.java
Step 3: Start Apache Tomcat
Navigate to <TOMCAT_DIR>\bin\.

Double-click startup.bat (or execute ./startup.bat from terminal).

Ensure the terminal logs conclude with:

Plaintext
INFO [main] org.apache.catalina.startup.Catalina.start Server startup in [x] milliseconds
Step 4: Open the Application
Open any web browser and visit:

Plaintext
http://localhost:8080/BMS/index.jsp
🧪 Testing Application Workflow
Register Customer: Go to Create Customer, fill in details, and click Register Customer.

Open Account: Copy the assigned CUST-XXXX ID, navigate to Open Account, enter the ID with an initial deposit, and note the assigned ACC-XXXXXXXXXX.

Perform Transactions: Use the Deposit Cash, Withdraw Cash, or Fund Transfer tabs using your account numbers.

View Statement: Click Account Statement, enter the Account Number, and review the ledger records.
