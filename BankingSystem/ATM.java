package bankingsystem;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Scanner;
// FILE 1: InsufficientFundsException
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
// FILE 2: InvalidPINException
class InvalidPINException extends Exception {

    public InvalidPINException(String message) {
        super(message);
    }
}
// FILE 3: Account
abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected int pin;
    protected ArrayList<String> transactionLog = new ArrayList<>();
    public Account(String accountNumber, double balance, int pin) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.pin = pin;
    }
    public boolean verifyPIN(int enteredPIN)
            throws InvalidPINException {
        if (pin != enteredPIN) {
            throw new InvalidPINException("Invalid PIN!");
        }
        return true;
    }
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionLog.add(
                    LocalDateTime.now()
                    + " | Deposit | Balance: " + balance);
            System.out.println("Deposit successful!");
        } else {
            System.out.println("Invalid amount!");
        }
    }
    public void withdraw(double amount)
            throws InsufficientFundsException {
        if (amount <= 0 || amount > balance) {
            throw new InsufficientFundsException(
                    "Insufficient funds or invalid amount!");
        }
        balance -= amount;
        transactionLog.add(
                LocalDateTime.now()
                + " | Withdrawal | Balance: " + balance);
        System.out.println("Withdrawal successful!");
    }
    public double getBalance() {
        return balance;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public abstract void calculateInterest();
    public void showTransactions() {
        System.out.println("\nTransaction History:");
        for (String transaction : transactionLog) {
            System.out.println(transaction);
        }
    }
}
// FILE 4: SavingsAccount
class SavingsAccount extends Account {
    private double interestRate;
    public SavingsAccount(String accountNumber,
                          double balance,
                          int pin,
                          double interestRate) {
        super(accountNumber, balance, pin);
        this.interestRate = interestRate;
    }
    @Override
    public void calculateInterest() {
        double interest = balance * interestRate / 100;
        balance += interest;
        transactionLog.add(
                LocalDateTime.now()
                + " | Interest | Balance: " + balance);
        System.out.println("Interest added: " + interest);
        System.out.println("New balance: " + balance);
    }
}
// FILE 5: CheckingAccount
class CheckingAccount extends Account {
    private double overdraftLimit;
    public CheckingAccount(String accountNumber,
                           double balance,
                           int pin,
                           double overdraftLimit) {
        super(accountNumber, balance, pin);
        this.overdraftLimit = overdraftLimit;
    }
    @Override
    public void withdraw(double amount)
            throws InsufficientFundsException {
        if (amount <= 0 || amount > balance + overdraftLimit) {
            throw new InsufficientFundsException(
                    "Overdraft limit exceeded!");
        }
        balance -= amount;
        transactionLog.add(
                LocalDateTime.now()
                + " | Withdrawal | Balance: " + balance);
        System.out.println("Withdrawal successful!");
    }
    @Override
    public void calculateInterest() {
        System.out.println(
                "No interest for checking account.");
    }
}
// FILE 6: Customer
class Customer {
    private String name;
    private String customerID;
    private Account account;
    public Customer(String name,
                    String customerID,
                    Account account) {
        this.name = name;
        this.customerID = customerID;
        this.account = account;
    }
    public String getName() {
        return name;
    }
    public String getCustomerID() {
        return customerID;
    }
    public Account getAccount() {
        return account;
    }
    public void showCustomerDetails() {
        System.out.println("\nCustomer Details");
        System.out.println("Name: " + name);
        System.out.println("Customer ID: " + customerID);
        System.out.println(
                "Account Number: "
                + account.getAccountNumber());
        System.out.println(
                "Balance: " + account.getBalance());
    }
}
// FILE 7: ATM (MAIN CLASS)
public class ATM {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("===== WELCOME TO ATM =====");
        System.out.print("Enter Customer Name: ");
        String name = input.nextLine();
        System.out.print("Enter Customer ID: ");
        String customerID = input.nextLine();
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");
        System.out.print("Enter choice: ");
        int choice = input.nextInt();
        System.out.print("Set your PIN: ");
        int pin = input.nextInt();
        Account account;
        if (choice == 1) {
            account = new SavingsAccount(
                    "SA101", 10000, pin, 5);
        } else if (choice == 2) {
            account = new CheckingAccount(
                    "CA101", 10000, pin, 5000);
        } else {
            System.out.println("Invalid account type!");
            input.close();
            return;
        }
        Customer customer = new Customer(
                name, customerID, account);
        System.out.println("\nAccount created successfully!");
        System.out.print("Enter PIN to login: ");
        int enteredPIN = input.nextInt();
        try {
            account.verifyPIN(enteredPIN);
            System.out.println("\nLogin successful!");
            int option;
            do {
                System.out.println("\n===== ATM MENU =====");
                System.out.println("1. Deposit Money");
                System.out.println("2. Withdraw Money");
                System.out.println("3. Check Balance");
                System.out.println("4. Calculate Interest");
                System.out.println("5. Transaction History");
                System.out.println("6. Customer Details");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                option = input.nextInt();
                try {
                    switch (option) {
                        case 1:
                            System.out.print("Enter amount: ");
                            double deposit = input.nextDouble();
                            account.deposit(deposit);
                            break;
                        case 2:
                            System.out.print("Enter amount: ");
                            double withdraw = input.nextDouble();
                            account.withdraw(withdraw);
                            break;
                        case 3:
                            System.out.println(
                                    "Current Balance: "
                                    + account.getBalance());
                            break;
                        case 4:
                            account.calculateInterest();
                            break;
                        case 5:
                            account.showTransactions();
                            break;
                        case 6:
                            customer.showCustomerDetails();
                            break;
                        case 0:
                            System.out.println(
                                    "Thank you for using our ATM!");
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                } catch (InsufficientFundsException e) {
                    System.out.println(
                            "Transaction Failed: " + e.getMessage());
                }
            } while (option != 0);
        } catch (InvalidPINException e) {
            System.out.println(
                    "Login Failed: " + e.getMessage());
        }
        input.close();
    }
}
