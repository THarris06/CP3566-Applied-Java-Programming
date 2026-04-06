package rooms.client.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.mariadb.jdbc.client.Client;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

@Named("friendBean")
@RequestScoped
public class Friend {
    @Inject private UserLogin login;
    private String message;
    private String token;
    private Connection conn;
    private Client client;
    private WebTarget base;
    private String targetUserName;

    
    @PostConstruct
    public void PostConstruct(){
        try {
            token = null;
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/GroupsAndFriends");
            this.conn = ds.getConnection();
        } catch (NamingException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @PreDestroy
    public void preDestroy(){
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int getUserIDByUserName(String userName) {
        try  (
            PreparedStatement stmt = conn.prepareStatement("SELECT id FROM users WHERE name = ?;")
        ) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            message = e.getMessage();
            return -1;
        }
        return 0; 
    }
    
    public void sendFriendRequest() {
        int sendingUID = login.getUserId();
        int receivingUID = getUserIDByUserName(targetUserName);
        if (receivingUID == -1) {
            message = "No user with username: " + targetUserName + " found in DB!";
            return;
        }

        int statusCode = base
            .path("" + sendingUID + "/")
            .path("" + receivingUID + "/")
            .path("" + login.getToken())
            .request(MediaType.TEXT_PLAIN)
            .get(Integer.class);
        message = switch(statusCode) {
            case 0 -> "Friend request sent!";
            case 1 -> "Invalid user credentials!";
            case 2 -> "Wrong user for login!";
            case 3 -> "Service was unable to connect to internal DB!";
            case 4 -> "Unable to process friend request!";
            default -> "Unknown error!";
        };
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }
}
