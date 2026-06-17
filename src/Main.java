import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        double balance = 0.0;
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
                }
            }
            else if (choice == 4) {
                System.out.println("Transaction history:");
                System.out.println(story);
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
}
