package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;
    public User (Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void register(){
        scanner.nextLine();
        System.out.println("Full name:");
        String full_name=scanner.nextLine();
        System.out.println("Email:");
        String email= scanner.nextLine();
        System.out.println("Password:");
        String password= scanner.nextLine();
        if(userExists(email)){
            System.out.println("User Already Exists for this Email ");
            return;
        }
        else {
           try {
               String register_query="INSERT into User (full_name, email, password) VALUES (?, ?, ?)";
               PreparedStatement preparedStatement=connection.prepareStatement(register_query);
               preparedStatement.setString(1,full_name);
               preparedStatement.setString(2,email);
               preparedStatement.setString(3,password);
              int row=preparedStatement.executeUpdate();
               if (row>0){
                   System.out.println("Registration Successful");
               }
               else {
                   System.out.println("Registration Failed");

               }
           } catch (SQLException e) {
              e.printStackTrace();
           }

        }
    }
    public String login(){
        scanner.nextLine();
        System.out.println("Email:");
        String email= scanner.nextLine();
        System.out.println("Password:");
        String password= scanner.nextLine();
        String login_query="SELECT * FROM user WHERE email= ? AND password= ?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next()){
                return email;
            }
            else {return null;}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean userExists(String email){
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
