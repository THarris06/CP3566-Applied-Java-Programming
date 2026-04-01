package rooms.rest.service;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("friends")
public class Friends {

    @Path("{sendingUID}/{receivingUID}/{token}")
    public int sendFriendRequest(
        @PathParam("sendingUID") int sendingUID,
        @PathParam("receivingUID") int receivingUID,
        @PathParam("token") String token
    ) {
        try {
            Context ctx  = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/GroupsAndFriends");
            try (
                Connection conn = ds.getConnection();
            ) {
                try (
                    PreparedStatement validateUserStmt = conn.prepareStatement(
                        "SELECT id FROM users WHERE token = ?"
                    );

                ) {
                    validateUserStmt.setString(1, token);
                    ResultSet rs = validateUserStmt.executeQuery();
                    if (!rs.next()) {
                        rs.close();
                        return 1;
                    }
                    int tokenUID = rs.getInt(1);
                    rs.close();
                    if (tokenUID != sendingUID) {
                        return 2;
                    }
                } catch (SQLException e) {
                    // TODO: handle exception
                }
            } catch (SQLException e) {
                // TODO: handle exception
            }
        } catch (NamingException e) {
            // TODO: handle exception
        }

    }

}
