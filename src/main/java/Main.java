import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        createDatabaseTable();
        double balance = loadBalance();
        Scanner check = new Scanner(System.in);
        String story = "";

        while (true) {
            System.out.println("1 Show Balance");
            System.out.println("2 Deposit Money");
            System.out.println("3 Withdraw Money");
            System.out.println("4 Transaction History");
            System.out.println("5 Exit");

            int choice = check.nextInt();

            if (choice == 1) {
                showBalance(balance);
            }
            else if (choice == 2) {
                System.out.println("Enter deposit amount:");
                double deposit = check.nextDouble();

                if (deposit <= 0) {
                    System.out.println("Invalid amount");
                } else {
                    balance = balance + deposit;
                    story = story + "Deposit: " + deposit + " Euro\n";
                    addTransaction("Deposit", deposit, balance);
                }
            }
            else if (choice == 3) {
                System.out.println("Enter withdrawal amount:");
                double withdraw = check.nextDouble();

                if (withdraw <= 0) {
                    System.out.println("Invalid amount");
                } else if (withdraw > balance) {
                    System.out.println("Insufficient balance");
                } else {
                    balance = balance - withdraw;
                    story = story + "Withdraw: " + withdraw + " Euro\n";
                    addTransaction("Withdraw", withdraw, balance);
                }
            }
            else if (choice == 4) {
                System.out.println("Transaction history:");
                printTransactionHistory();
            }
            else if (choice == 5) {
                System.out.println("Exit program");
                break;
            }
            else {
                System.out.println("Invalid choice");
            }
        }
    }

    public static void showBalance(double balance) {

        System.out.println("Current balance: " + balance + " Euro");
    }

    public static void addTransaction(String type, double amount, double balanceAfter) {
        String sql = "INSERT INTO transactions(type, amount, balance_after) VALUES(?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type);
            statement.setDouble(2, amount);
            statement.setDouble(3, balanceAfter);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Could not save transaction");
        }
    }

    public static void printTransactionHistory() {
        String sql = "SELECT type, amount FROM transactions ORDER BY id";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String type = resultSet.getString("type");
                double amount = resultSet.getDouble("amount");

                System.out.println(type + ": " + amount + " Euro");
            }

        } catch (SQLException e) {
            System.out.println("No transactions found");
        }
    }

    public static double loadBalance() {
        String sql = "SELECT balance_after FROM transactions ORDER BY id DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getDouble("balance_after");
            }

        } catch (SQLException e) {
            System.out.println("Error while loading balance");
        }

        return 0.0;
    }

    public static void createDatabaseTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT NOT NULL, " +
                "amount REAL NOT NULL, " +
                "balance_after REAL NOT NULL" +
                ")";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println("Error while creating database table");
        }
    }
}
