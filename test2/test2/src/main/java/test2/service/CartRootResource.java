package test2.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import test2.model.Cart;
import test2.model.Item;

//! Challenge level: Easy
// TODO: set the path for this class to "service/cart"
@Path("service/cart")
public class CartRootResource {

    private Connection getConnection(){
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Test2");
            return ds.getConnection();
        } catch (Exception e) {
            return null;
        }
    }

    private Cart getCartFromId(long cartId, Connection conn){
        System.out.println("CartRootResource.getCartFromId: Creating Select statement");
        try (
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT i.id, i.name, i.brand, i.size, i.unitCost"
                + " FROM cart_entry ce"
                + " INNER JOIN items i ON ce.itemID=i.id"
                + " WHERE ce.cartID=?"
            )
        ){
            System.out.println("CartRootResource.getCartFromId: Setting parameter markers");
            stmt.setLong(1, cartId);
            System.out.println("CartRootResource.getCartFromId: Executing Query");
            try(
                ResultSet rs = stmt.executeQuery()
            ){
                List<Item> items = new ArrayList<>();
                System.out.println("CartRootResource.getCartFromId: processing results");
                int itemCount = 0;
                while (rs.next()) {
                    System.out.println("CartRootResource.getCartFromId: adding item "+itemCount++);
                    items.add(
                        Item.makeItem(
                            rs.getLong(1), 
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getLong(5)
                        )
                    );
                }
                System.out.println("CartRootResource.getCartFromId: calculating subTotal");
                long subTotal = items.stream().reduce(0l, (acc, item) -> acc + item.getUnitCost(), Long::sum);
                System.out.println("CartRootResource.getCartFromId: returning cart");
                return Cart.makeCart(cartId, items, subTotal);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Cart makeNewCartForUser(long userId, Connection conn){
        System.out.println("CartRootResource.makeNewCartForUser: Creating Insert statement");
        try (
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO carts (userId) VALUES (?)"
            )
        ){
            System.out.println("CartRootResource.makeNewCartForUser: Setting parameter markers");
            insertStmt.setLong(1, userId);
            System.out.println("CartRootResource.makeNewCartForUser: Executing update");
            int updates = insertStmt.executeUpdate();
            if (updates != 1) {
                System.out.println("CartRootResource.makeNewCartForUser: Number of affected rows was not 1");
                return null;
            }
            System.out.println("CartRootResource.makeNewCartForUser: Creating Select statement");
            try (
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id FROM carts WHERE userId=?;"
                )
            ){
                System.out.println("CartRootResource.makeNewCartForUser: Setting parameter markers");
                stmt.setLong(1, userId);
                System.out.println("CartRootResource.makeNewCartForUser: Executing query");
                try(ResultSet rs = stmt.executeQuery()){
                    if (rs.next()) {
                        System.out.println("CartRootResource.makeNewCartForUser: creating cart from resultset's id");
                        return getCartFromId(rs.getLong(1), conn);
                    } else {
                        // Shouldn't happen, but just in case.
                        System.out.println("CartRootResource.makeNewCartForUser: result set had no entries");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //! Challenge level: Medium
    // TODO: set the path for this method to "getOrMakeByUser/{userId}", have it produce "application/json" and handle GET requests
    @Path("getOrMakeByUser/{userId}")
    @Produces("application/json")
    @GET
    public Cart getOrMakeCart(
        // TODO: annotate this parameter as the "userId" path parameter
        @PathParam("userId") long userId
    ){
        Connection conn = getConnection();
        System.out.println("CartRootResource.getOrMakeCart: Getting Connection");
        if (conn == null)
            return null;
        System.out.println("CartRootResource.getOrMakeCart: Creating Select statement");
        try (
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id FROM carts WHERE userId=?;"
            )
        ){
            System.out.println("CartRootResource.getOrMakeCart: Setting Select statement values");
            stmt.setLong(1, userId);
            System.out.println("CartRootResource.getOrMakeCart: Executing Select statement");
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    System.out.println("CartRootResource.getOrMakeCart: found a cart id");
                    return getCartFromId(rs.getLong(1), conn);
                } else {
                    System.out.println("CartRootResource.getOrMakeCart: no cart id for user");
                    return makeNewCartForUser(userId, conn);
                }
            }
        } catch (Exception e) {
            System.out.println("CartRootResource.getOrMakeCart: Exception getting or making cart");
            e.printStackTrace();
            return null;
        } finally { 
            try {
                conn.close();
            } catch (SQLException e) {}
        }
    }

    //! Challenge level: Medium
    // TODO: set the path for this method to "add/{cartId}/{itemId}", have it produce "application/json" and handle GET requests
    @Path("add/{cartId}/{itemId}")
    @Produces("application/json")
    @GET
    public Cart addItemToCart(
        // TODO: annotate this parameter as the "cartId" path parameter
        @PathParam("cartId") long cartId,
        // TODO: annotate this parameter as the "itemId" path parameter
        @PathParam("itemId") long itemId
    ){
        Connection conn = getConnection();
        System.out.println("CartRootResource.addItemToCart: Getting Connection");
        if (conn == null)
            return null;
        System.out.println("CartRootResource.addItemToCart: Creating Insert statement");
        try (
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO cart_entry"
                + " (cartId, itemId)"
                + " VALUES"
                + " (?, ?)"
                + ";"
            )
        ){
            System.out.println("CartRootResource.addItemToCart: Setting Insert statement values");
            stmt.setLong(1, cartId);
            stmt.setLong(2, itemId);
            System.out.println("CartRootResource.addItemToCart: Executing Insert statement");
            stmt.executeUpdate();
            System.out.println("CartRootResource.addItemToCart: Calling getCartFromId");
            return getCartFromId(cartId, conn);
        } catch (Exception e) {
            System.out.println("CartRootResource.addItemToCart: Exception raised");
            e.printStackTrace();
            return null;
        } finally { 
            try {
                conn.close();
            } catch (SQLException e) {}
        }
    }
}
