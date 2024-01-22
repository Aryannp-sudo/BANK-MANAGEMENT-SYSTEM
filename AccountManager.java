package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public  class AccountManager {
    private Connection connection;
    private  Scanner scanner;
    public AccountManager (Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void creditMoney(long acc_num) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount");
        double amount=scanner.nextDouble();
        System.out.println("Enter Security key");
        String security_key=scanner.next();
        String sql="SELECT * from accounts WHERE acc_num =? and security_pin=?";
        try {
            connection.setAutoCommit(false);
            if(acc_num!=0){
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setDouble(1,acc_num);
                preparedStatement.setString(2,security_key);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    String query="UPDATE INTO accounts balance=balance+? where account_number=?";
                    PreparedStatement ps=connection.prepareStatement(query);
                    ps.setDouble(1,amount);
                    ps.setLong(2,acc_num);
                    int affected_rows=ps.executeUpdate();
                    if (affected_rows>0){
                        System.out.println("Rs."+amount+"has been credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);

                    }else {
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else {
                    System.out.println("Invalid Security Pin");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }
    public void debitMoney(long acc_num) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount");
        double amount=scanner.nextDouble();
        System.out.println("Enter Security key");
        String security_key=scanner.next();
        String sql="SELECT * from accounts WHERE account_number =? and security_pin=?";
        try {
            connection.setAutoCommit(false);
            if(acc_num!=0){
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setDouble(1,acc_num);
                preparedStatement.setString(2,security_key);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    double balance=resultSet.getDouble("balance");
                    if(balance>=amount){
                    String query="UPDATE INTO accounts balance=balance-? where account_number=?";
                    PreparedStatement ps=connection.prepareStatement(query);
                    ps.setDouble(1,amount);
                    ps.setLong(2,acc_num);
                    int affected_rows=ps.executeUpdate();
                    if (affected_rows>0){
                        System.out.println("Rs."+amount+"has been debited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);

                    }else {
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }

                }
                    else {
                        System.out.println("Insufficient balance");}
                }
                else {
                    System.out.println("Invalid Security Pin");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.setAutoCommit(true);
    }
    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.println("Enter Security pin");
        String security_pin=scanner.nextLine();
        String sql="SELECT balance from accounts where account_number=? and security_pin=?";
        double balance;
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                 balance=resultSet.getDouble("balance");
                System.out.println("your balance is "+balance);
            }
            else {
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }






    public void transferMoney (long sender_account_number){
        scanner.nextLine();
        System.out.println("Enter Receiver's account number: ");
        long receiver_account_number =scanner.nextLong();
        System.out.println("Enter Amount:");
        double amount=scanner.nextDouble();
        System.out.println("Enter Security pin:");
        String security_pin=scanner.nextLine();

        String sql="SELECT * from accounts WHERE account_number=? and security_pin=?";
        try {
            connection.setAutoCommit(false);
            if (sender_account_number!=0 && receiver_account_number!=0){
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,sender_account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                double balance =resultSet.getDouble("balance");
                if (balance>=amount){
                    String creditQuery="UPDATE INTO accounts balance=balance+? where account_number=?";
                    String debitQuery="UPDATE INTO accounts balance=balance-? where account_number=?";
                    PreparedStatement preparedStatement1= connection.prepareStatement(creditQuery);
                    PreparedStatement preparedStatement2= connection.prepareStatement(debitQuery);
                    preparedStatement1.setLong(2,receiver_account_number);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement2.setLong(2,sender_account_number);
                    preparedStatement2.setDouble(1,amount);
                    int row_affected1=preparedStatement1.executeUpdate();
                    int row_affected2=preparedStatement2.executeUpdate();
                    if (row_affected1>0 && row_affected2>0){
                        System.out.println("transaction Successful");
                        System.out.println("rs."+balance+"transferred successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                    else {
                        System.out.println("transaction Failed");
                        connection.rollback();

                    }


                }
                else {
                    System.out.println("insufficient balance");
                }
            }
            else {
                System.out.println("Invalid Security pin");
            }}
            else {
                System.out.println("Receiver or Sender Account Number Invalid");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
