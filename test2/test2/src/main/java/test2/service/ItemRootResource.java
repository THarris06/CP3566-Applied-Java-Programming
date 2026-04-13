package test2.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
// TODO: set the path for this class to "service/item"
@Path("service/item")
public class ItemRootResource {

    private Connection getConnection(){
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Test2");
            return ds.getConnection();
        } catch (Exception e) {
            return null;
        }
    }


    //! Challenge level: Medium
    // TODO: set the path for this method to "{brand}/{name}/{size}", have it produce "application/json" and handle GET requests
    @Path("{brand}/{name}/{size}")
    @Produces("application/json")
    @GET
    public Item getItem(
        // TODO: annotate this parameter as the "brand" path parameter
        @PathParam("brand") String brand,
        // TODO: annotate this parameter as the "name" path parameter
        @PathParam("name") String name,
        // TODO: annotate this parameter as the "size" path parameter
        @PathParam("size") String size
    ){
        Connection conn = getConnection();
        System.out.println("ItemRootResource.getItem: Getting connection");
        if (conn == null)
            return null;
        System.out.println("ItemRootResource.getItem: Creating PreparedStatement");
        try (
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, unitCost FROM items WHERE"
                + " brand=?"
                + " AND name=?"
                + " AND size=?"
                + ";"
            )
        ){
            System.out.println("ItemRootResource.getItem: Setting parameter markers");
            stmt.setString(1, brand);
            stmt.setString(2, name);
            stmt.setString(3, size);
            System.out.println("ItemRootResource.getItem: Executing Query");
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    System.out.println("ItemRootResource.getItem: returning item");
                    return Item.makeItem(rs.getLong(1), name, brand, size, rs.getLong(2));
                } else {
                    System.out.println("ItemRootResource.getItem: no item in DB for " + brand+":"+name+":"+size);
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println("ItemRootResource.getItem: exction raised");
            e.printStackTrace();
            return null;
        } finally { 
            try {
                conn.close();
            } catch (SQLException e) {}
        }
    }
    
}
