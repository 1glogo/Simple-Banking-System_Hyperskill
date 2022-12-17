package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlAddQueryMethods {

    //General Connection to database
    private Connection connect(String url) {
        // SQLite's connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //Insert card to database
    public void insert(String number, String pin, String url) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("We are in here");
            System.out.println(e.getMessage());
        }
    }
    //getting card matching card number
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
    //increasing one's balance
    public void addIncome (String cardNumber, int addValue, String url){
        String sql = "UPDATE card SET balance = (balance+?) WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,addValue);
            pstmt.setString(2,cardNumber);

            pstmt.executeUpdate();

            System.out.println("Income was added");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Reduce card balance based on vardNumber and reduction value provided
    public void reduceBalance (String cardNumber, int redValue, String url){
        String sql = "UPDATE card SET balance = (balance-?) WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,redValue);
            pstmt.setString(2,cardNumber);

            pstmt.executeUpdate();

            System.out.println("Income was added");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Increasing balance as part of transaction
    public void increaseBalance (String cardNumber, int addValue, String url){
        String sql = "UPDATE card SET balance = (balance+?) WHERE number = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,addValue);
            pstmt.setString(2,cardNumber);

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

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
