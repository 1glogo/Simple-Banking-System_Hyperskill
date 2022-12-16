package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlAddQueryMethods {

    //General Connection
    private Connection connect(String url) {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //Insert to Table
    public void insert(String number, String pin, String url) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Query from Table
    public List<String> getAllCardNumbers(String cardNumber, String url){
        String sql = "SELECT number"
                + "FROM card WHERE number = '?'";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,cardNumber);

            ResultSet rs  = pstmt.executeQuery();

            List<String> resultList = new ArrayList<>();

            // loop through the result set
            while (rs.next()) {
                resultList.add(rs.getString("number"));
            }

            return resultList;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Card> getMatchingCard(String cardNumber, String url){
        String sql = "SELECT *"
                + "FROM card WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,cardNumber);

            ResultSet rs  = pstmt.executeQuery();

            List<Card> resultList = new ArrayList<>();

            // loop through the result set
            while (rs.next()) {
                Card newCard = new Card(rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));

                resultList.add(newCard);
            }

            return resultList;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    //Balance methods
    public void addIncome (String cardNumber, int addValue, String url){
        String sql = "UPDATE card SET balance = (balance+?) WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,addValue);
            pstmt.setString(2,cardNumber);

            //Important to update the below to execUpdate;
            pstmt.executeUpdate();

            System.out.println("Income was added");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void reduceBalance (String cardNumber, int redValue, String url){
        String sql = "UPDATE card SET balance = (balance-?) WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,redValue);
            pstmt.setString(2,cardNumber);

            //Important to update the below to execUpdate;
            pstmt.executeUpdate();

            System.out.println("Income was added");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void increaseBalance (String cardNumber, int addValue, String url){
        String sql = "UPDATE card SET balance = (balance+?) WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,addValue);
            pstmt.setString(2,cardNumber);

            //Important to update the below to execUpdate;
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //DELETE ACCOUNT
    public void deleteCard (String cardNumber, String url){
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,cardNumber);

            //Important to update the below to execUpdate;
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
