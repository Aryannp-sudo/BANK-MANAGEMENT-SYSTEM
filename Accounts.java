package banking;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private static Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    public long open_account(String email){
        if(!accountExists(email)) {
            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();
            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist");

    }
    public long getAccountNumber(String email){
        String sql="Select account_number FROM accounts where email=?";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                long acc=resultSet.getLong("account_number");
                return acc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account with this email address doesnt exist");
    }
    public long generateAccountNumber(){
        String sql="Select account_number from accounts ORDER BY account_number DESC LIMIT 1";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            if (resultSet.next()){
                long ac=resultSet.getLong("account_number");
                return  ac+1;
            }
            else {
                return 10000100;
            }

        } catch (SQLException e) {
          e.printStackTrace();
        }
        return 1000100;
    }
    public boolean accountExists(String email){
        try {
            String query="SELECT * from user WHERE email=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            int row_affected=preparedStatement.executeUpdate();
            if (row_affected>0){
                return true;
            }
            else {return false;}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }return false;
    }
}
