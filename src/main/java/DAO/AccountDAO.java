package DAO;

import Model.Account;
import Util.ConnectionUtil;

//import static org.mockito.ArgumentMatchers.nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account createAccount(Account account) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account getAccountByUsernameAndPassword(String username, String password) {
        Account account = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    public static List<Integer> getAllAccountsIds(){
        List<Integer> l=new ArrayList<>();
        try(Connection conn = ConnectionUtil.getConnection()){
            String s="select account_id from account;";
            Statement ps=conn.createStatement();
            ResultSet rs=ps.executeQuery(s);
            while(rs.next()){
                l.add(rs.getInt(1));
            }
        }
        catch(Exception e){

        }
        return l;
    }
    
}
