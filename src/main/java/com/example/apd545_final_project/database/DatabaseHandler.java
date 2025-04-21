package com.example.apd545_final_project.database;

import com.example.apd545_final_project.model.Budget;
import com.example.apd545_final_project.model.Transaction;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class DatabaseHandler {
    private static final Logger LOGGER = Logger.getLogger(DatabaseHandler.class.getName());
    private static final String DB_NAME = "finance.db";
    private static final String DB_PATH = "src/data/" + DB_NAME;
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static Connection connection;

    static {
        try {
            setupLogger();
        } catch (Exception e) {
            System.err.println("Logger setup failed: " + e.getMessage());
        }
    }

    public DatabaseHandler() {
        // Prevent instantiation
    }

    private static void setupLogger() throws Exception {
        File logsDir = new File("src/data");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        FileHandler fileHandler = new FileHandler("src/data/finance_logs.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(fileHandler);

        LOGGER.info("Logger initialized for DatabaseHandler");
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                File dbFile = new File(DB_PATH);
                boolean firstRun = !dbFile.exists();

                LOGGER.info("Establishing connection to SQLite...");
                connection = DriverManager.getConnection(DB_URL);

                if (firstRun) {
                    LOGGER.info("Database file not found. First run detected. Initializing tables...");
                    initializeDatabase();
                }

                LOGGER.info("Connection established successfully");
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "JDBC driver not found", e);
                throw new SQLException("JDBC driver not found", e);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error connecting to database", e);
                throw e;
            }
        }
        return connection;
    }

    private static void initializeDatabase() throws SQLException {
        String createTransactions = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                date TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT NOT NULL,
                description TEXT
            );
        """;
        
        String createBudgets = """
            CREATE TABLE IF NOT EXISTS budgets (
                category TEXT PRIMARY KEY,
                budget_limit REAL NOT NULL
            );
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTransactions);
            stmt.execute(createBudgets);
            LOGGER.info("Tables created successfully");
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to close connection", e);
        }
    }

    public static void insertTransaction(Transaction tx) throws SQLException {
        String sql = "INSERT INTO transactions(type, date, amount, category, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tx.getType());
            stmt.setString(2, tx.getDate().toString());
            stmt.setDouble(3, tx.getAmount());
            stmt.setString(4, tx.getCategory());
            stmt.setString(5, tx.getDescription());
            stmt.executeUpdate();

            LOGGER.info("Transaction inserted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to insert transaction", e);
            throw e;
        }
    }
    
    public void updateTransaction(int id, Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET type=?, date=?, amount=?, category=?, description=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getType());
            pstmt.setString(2, transaction.getDate().toString());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getCategory());
            pstmt.setString(5, transaction.getDescription());
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
            LOGGER.info("Transaction Updated successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update transaction", e);
            throw e;
        }
    }

    public void deleteTransaction(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
         
            LOGGER.info("Transaction Deleted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete transaction", e);
            throw e;
        }
    }

    public static List<Transaction> fetchTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaction tx = new Transaction(
                	rs.getInt("id"),
                    rs.getString("type"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    rs.getString("description")
                );
                transactions.add(tx);
            }

            LOGGER.info("Fetched " + transactions.size() + " transactions from DB");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch transactions", e);
            throw e;
        }

        return transactions;
    }
    
    public List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM transactions";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }

    
 // === Budget Table ===
    public List<Budget> fetchBudgets() throws SQLException {
    	List<Budget> budgets = new ArrayList<>();
        String query = "SELECT * FROM budgets";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String category = rs.getString("category");
                double limit = rs.getDouble("budget_limit");
                budgets.add(new Budget(category, limit));
            }
        }
        return budgets;
    }

    public void insertOrUpdateBudget(String category, double limit) throws SQLException {
        String sql = "INSERT INTO budgets (category, budget_limit) VALUES (?, ?) ON CONFLICT(category) DO UPDATE SET budget_limit=?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setDouble(2, limit);
            pstmt.setDouble(3, limit);
            pstmt.executeUpdate();
            LOGGER.info("Budget inserted/updated for category: " + category);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to insert/update budget", e);
            throw e;
        }
    }

    public double getSpentAmount(String category, int month, int year) throws SQLException {
        String sql = "SELECT SUM(amount) FROM transactions WHERE category = ? AND strftime('%m', date) = ? AND strftime('%Y', date) = ? AND type='Expense'";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, String.format("%02d", month));
            pstmt.setString(3, String.valueOf(year));
            ResultSet rs = pstmt.executeQuery();
            double spent = rs.next() ? rs.getDouble(1) : 0;
            LOGGER.info("Total spent in " + category + " for " + month + "/" + year + " is " + spent);
            return spent;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get spent amount", e);
            throw e;
        }
    }

    public double getBudgetLimit(String category) throws SQLException {
        String sql = "SELECT budget_limit FROM budgets WHERE category = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            double limit = rs.next() ? rs.getDouble(1) : 0;
            LOGGER.info("Fetched budget limit for category '" + category + "': " + limit);
            return limit;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch budget limit", e);
            throw e;
        }
    }
}
