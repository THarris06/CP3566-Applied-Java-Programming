package assignment3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;  
import java.sql.SQLException;
import javax.sql.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import assignment3.model.Car;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.util.ArrayList;

// TODO: Annotate this class with a Path annotation, and set the path to "cars"
@Path("cars")
public class App {

    // TODO: Make a function called getCars that returns a List of Cars
    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "all"
    // TODO: Annotate that functions with a GET annotation
    @Produces("application/json")
    @Path("all")
    @GET
    public ArrayList<Car> getCars() throws SQLException {
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        ArrayList<Car> list_of_cars = new ArrayList<Car>();
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars;
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Cars");
            // TODO: Use a try-with-resources block to create and auto-close:
            try (
                // TODO: a connection from the data
                Connection conn = ds.getConnection();
                // TODO: a prepared statement from the connection that selects the make, model and year columns from the cars table
                PreparedStatement stmt = conn.prepareStatement("SELECT make, model, year FROM cars");
                // TODO: a result set from executing the prepared statement as a query
                ResultSet rs = stmt.executeQuery();
            ) {
            // TODO: then:
                // TODO: while the result set still has next rows to process
                while (rs.next()) {
                    // TODO: Create a new Car object
                    Car car = new Car();
                    // TODO: set its make to the one in this row of the result set
                    car.setMake(rs.getString("make"));
                    // TODO: set its model to the one in this row of the result set
                    car.setModel(rs.getString("model"));
                    // TODO: set its year to the one in this row of the result set
                    car.setYear(rs.getInt("year"));
                    // TODO: add the car to the list you made at the start of this method
                    list_of_cars.add(car);
                }
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(NamingException e) {
            e.printStackTrace();
        }
        // TODO: return the list you made at the start of this function
        return list_of_cars;
    }

    // TODO: Make a function called getCarByMake that returns a List of Cars
    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "{make}"
    // TODO: Annotate that functions with a GET annotation
    // TODO: give it a string parameter for the make of the car and annotate that parameter with PathParam("make")
    @Produces("application/json")
    @Path("{make}")
    @GET
    public ArrayList<Car> getCarByMake(@PathParam("make") String make) throws SQLException {
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        ArrayList<Car> list_of_cars = new ArrayList<Car>();
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Cars");
            // TODO: Use a try-with-resources block to create and auto-close:
            try (
                // TODO: a connection from the data
                Connection conn = ds.getConnection();
                // TODO: a prepared statement from the connection that selects the model and year columns from the cars table where the make equals a parameter marker
                PreparedStatement stmt = conn.prepareStatement("SELECT model, year FROM cars WHERE make = ?");
            ) {
            // TODO: then:
                // TODO: Set the parameter marker to the string parameter passed into this method
                stmt.setString(1, make);
                // TODO: Use a try-with-resources block to create and auto-close:
                try (
                    // TODO: a result set from executing the prepared statement as a query
                    ResultSet rs = stmt.executeQuery();
                ) {
                // TODO: then
                    // TODO: while the result set still has next rows to process
                    while (rs.next()) {
                        // TODO: Create a new Car object
                        Car car = new Car();
                        // TODO: set its make to the one passed into this method
                        car.setMake(make);
                        // TODO: set its model to the one in this row of the result set
                        car.setModel(rs.getString("model"));
                        // TODO: set its year to the one in this row of the result set
                        car.setYear(rs.getInt("year"));
                        // TODO: add the car to the list you made at the start of this method
                        list_of_cars.add(car);
                    }
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch(NamingException e) {
            e.printStackTrace();
        }
        // TODO: return the list you made at the start of this function
        return list_of_cars;
    }

    // TODO: Make a function called getCarByMakeAndModel that returns a List of Cars
    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "{make}/{model}"
    // TODO: Annotate that functions with a GET annotation
    // TODO: give it a string parameter for the make of the car and annotate that parameter with PathParam("make")
    // TODO: give it a string parameter for the model of the car and annotate that parameter with PathParam("model")
    @Produces("application/json")
    @Path("{make}/{model}")
    @GET
    public ArrayList<Car> getCarByMakeAndModel(@PathParam("make") String make, @PathParam("model") String model) throws SQLException {
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        ArrayList<Car> list_of_cars = new ArrayList<Car>();
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Cars");
            // TODO: Use a try-with-resources block to create and auto-close:
            try (
                // TODO: a connection from the data
                Connection conn = ds.getConnection();
                // TODO: a prepared statement from the connection that selects the year column from the cars table where the make and model equal parameter markers
                PreparedStatement stmt = conn.prepareStatement("SELECT year FROM cars WHERE make = ? AND model = ?");
            ) {
            // TODO: then:

                // TODO: Set the parameter marker for the make to the make string parameter passed into this method
                stmt.setString(1, make);
                // TODO: Set the parameter marker for the model to the model string parameter passed into this method
                stmt.setString(2, model);
                // TODO: Use a try-with-resources block to create and auto-close:
                try (
                    // TODO: a result set from executing the prepared statement as a query
                    ResultSet rs = stmt.executeQuery();
                ) {
                    // TODO: then
                    // TODO: while the result set still has next rows to process
                    while (rs.next()) {
                        // TODO: Create a new Car object
                        Car car = new Car();
                        // TODO: set its make to the one passed into this method
                        car.setMake(make);
                        // TODO: set its model to the one passed into this method
                        car.setModel(model);
                        // TODO: set its year to the one in this row of the result set
                        car.setYear(rs.getInt("year"));
                        // TODO: add the car to the list you made at the start of this method
                        list_of_cars.add(car);
                    }
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(NamingException e) {
            e.printStackTrace();
        }
        // TODO: return the list you made at the start of this function
        return list_of_cars;
    }

    // TODO: Make a function called getCarByYear that returns a List of Cars
    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "yearof/{year}"
    // TODO: Annotate that functions with a GET annotation
    // TODO: give it an int parameter for the year of the car and annotate that parameter with PathParam("year")
    @Produces("application/json")
    @Path("yearof/{year}")
    @GET
    public ArrayList<Car> getCarByYear(@PathParam("year") int year) throws SQLException {
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        ArrayList<Car> list_of_cars = new ArrayList<Car>();
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Cars");
            // TODO: Use a try-with-resources block to create and auto-close:
            try (
                // TODO: a connection from the data
                Connection conn = ds.getConnection();
                // TODO: a prepared statement from the connection that selects the make and model columns from the cars table where the year equals a parameter marker
                PreparedStatement stmt = conn.prepareStatement("SELECT make, model FROM cars WHERE year = ?");
            ) {
            // TODO: then:
                // TODO: Set the parameter marker to the int parameter passed into this method
                stmt.setInt(1, year);
                // TODO: Use a try-with-resources block to create and auto-close:
                try (
                    // TODO: a result set from executing the prepared statement as a query
                    ResultSet rs = stmt.executeQuery();
                ) {
                    // TODO: then
                    while (rs.next()) {
                        // TODO: Create a new Car object
                        Car car = new Car();
                        // TODO: set its make to the one in this row of the result set
                        car.setMake(rs.getString("make"));
                        // TODO: set its model to the one in this row of the result set
                        car.setModel(rs.getString("model"));
                        // TODO: set its year to the one passed into this method
                        car.setYear(year);
                        // TODO: add the car to the list you made at the start of this method
                        list_of_cars.add(car);
                    }
            // TODO: Catch NamingExceptions and SQLExceptions
                // TODO: print the stack trace of the exception
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(NamingException e) {
            e.printStackTrace();
        }
        // TODO: return the list you made at the start of this function
        return list_of_cars;
    }

    // TODO: Make a function called postCar that returns a String
    // TODO: Annotate that functions with a Consumes annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to ""
    // TODO: Annotate that functions with a POST annotation
    // TODO: give it a Car parameter
    @Consumes("application/json")
    @Path("")
    @POST
    public String postCar(Car car) throws SQLException {
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Cars");
            // TODO: Use a try-with-resources block to create and auto-close:
            try (
                // TODO: a connection from the data
                Connection conn = ds.getConnection();
                // TODO: a prepared statement from the connection that inserts into cars's make, model and year columns the values of 3 parameter markers
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO cars (make, model, year) VALUES (?, ?, ?)")
            ) {
            // TODO: then:
                // TODO: get the make from the car passed into this method, and set it to the parameter marker for the make column
                stmt.setString(1, car.getMake());
                // TODO: get the model from the car passed into this method, and set it to the parameter marker for the model column
                stmt.setString(2, car.getModel());
                // TODO: get the year from the car passed into this method, and set it to the parameter marker for the year column
                stmt.setInt(3, car.getYear());
                // TODO: create an int and set it to the result of calling executeUpdate on the prepared statement
                int rows = stmt.executeUpdate();
                // TODO: if the int equals 1, return "Car posted successfully", otherwise return "Failed to post new Car"
                if (rows == 1) {
                    return "Car posted successfully";
                } else {
                    return "Failed to post new Car";
                }
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
            // TODO: return "Failed to post new Car"
            } catch (SQLException e) {
                e.printStackTrace();
                return "Failed to post new Car";
            }
        } catch(NamingException e) {
            e.printStackTrace();
            return "Failed to post new Car";
        }
    }
}
