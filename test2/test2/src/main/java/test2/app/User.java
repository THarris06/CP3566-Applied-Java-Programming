package test2.app;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.naming.Context;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//! Challenge level: Easy
// TODO: Name this the "userLoginBean" and make it session scoped
@Named("userLoginBean")
@SessionScoped
public class User implements Serializable{
    @NotNull
    private String userName;
    @NotNull
    @NotBlank
    @Size(min=12,max=36)
    private String userPassword;
    private String token;
    private String message;
    private long userId;
    private Connection conn;

    private boolean verifyPassword(byte[] bytes) throws UnsupportedEncodingException{
        return BCrypt.verifyer().verify(userPassword.getBytes("UTF-16"), bytes).verified;
    }

    private byte[] getHashedPassword(String userPassword) throws UnsupportedEncodingException{
        return BCrypt.withDefaults().hash(12, userPassword.getBytes("UTF-16"));
    }

    //! Challenge level: Easy
    // TODO: Make this method the post construct method
    @PostConstruct
    public void openConnection(){
        try{
            token = null;
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Test2");
            conn = ds.getConnection();
        } catch (NamingException | SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //! Challenge level: Easy
    // TODO: Make this method the pre destroy method
    @PreDestroy
    public void closeConnection(){
        if (conn != null) {
            try{
                conn.close();
            } catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void updateToken() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id, token, PW_Hash FROM users WHERE username=?;")) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next() && verifyPassword(rs.getBytes("PW_Hash"))){
                    token = rs.getString("token");
                    userId = rs.getLong("id");
                    message="";
                } else {
                    message="Invalid Login";
                }
            }
        } catch (SQLException | UnsupportedEncodingException e) {
            message=e.getMessage();
        }
    }

    public void signup() {
        try (
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users"
                + " (username, PW_Hash, token)"
                + " VALUES"
                + " (?, ?, SHA2(RAND(), 256))"
                + ";"
            );
        ){
            byte[] hash = getHashedPassword(userPassword);
            stmt.setString(1, userName);
            stmt.setBytes(2, hash);
            int updates = stmt.executeUpdate();
            if(updates != 1){
                message="Failed to create new user: " + userName;
            } else {
                message="Successfully created new user: " + userName;
            }
        } catch (SQLException | UnsupportedEncodingException e) {
            message = e.getMessage();
        }
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getToken() {
        return token;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getMessage() {
        return message;
    }
}

