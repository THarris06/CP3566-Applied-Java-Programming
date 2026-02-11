
package assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import assignment.parsing.Order;
import assignment.parsing.OrderItem;
import assignment.parsing.OrderItemRefund;
import assignment.parsing.Product;
import assignment.parsing.WebsitePageview;
import assignment.parsing.WebsiteSession;

public class App {

    public static void createTables(Connection conn) {
        // TODO: Use the following SQL Strings to create tables using PreparedStatements
        String makeUsersString = 
            "CREATE TABLE IF NOT EXISTS users ("+
            "  user_id INT AUTO_INCREMENT PRIMARY KEY"+
            ");";
        String makeProductsString = 
            "CREATE TABLE IF NOT EXISTS products ("+
            "  product_id INT AUTO_INCREMENT PRIMARY KEY, "+
            "  product_name VARCHAR(50), "+
            "  created_at TIMESTAMP"+
            ");";
        String makeWebsiteSessionsString = 
            "CREATE TABLE IF NOT EXISTS website_sessions ("+
            "  website_session_id INT AUTO_INCREMENT PRIMARY KEY, "+
            "  user_id INT REFERENCES users(user_id), "+
            "  is_repeat_session INT, "+
            "  utm_source VARCHAR(50), "+
            "  utm_campaign VARCHAR(50), "+
            "  utm_content VARCHAR(50), "+
            "  device_type VARCHAR(50), "+
            "  http_referer VARCHAR(50), "+
            "  created_at TIMESTAMP"+
            ");";
        String makeWebsitePageviewsString = 
            "CREATE TABLE IF NOT EXISTS website_pageviews ("+
            "  website_pageview_id INT AUTO_INCREMENT PRIMARY KEY,"+
            "  website_session_id INT REFERENCES website_sessions(website_session_id),"+
            "  pageview_url VARCHAR(50),"+
            "  created_at TIMESTAMP"+
            ");";
        String makeOrdersString = 
            "CREATE TABLE IF NOT EXISTS orders ("+
            "  order_id INT AUTO_INCREMENT PRIMARY KEY,"+
            "  website_session_id INT REFERENCES website_sessions(website_session_id), "+
            "  user_id INT REFERENCES users(user_id), "+
            "  primary_product_id INT REFERENCES products(product_id), "+
            "  items_purchased INT, "+
            "  price_usd DOUBLE, "+
            "  cogs_usd DOUBLE, "+
            "  created_at TIMESTAMP"+
            ");";
        String makeOrderItemsString = 
            "CREATE TABLE IF NOT EXISTS order_items ("+
            "  order_item_id INT AUTO_INCREMENT PRIMARY KEY,"+
            "  order_id INT REFERENCES orders(order_id), "+
            "  product_id INT REFERENCES products(product_id),"+
            "  is_primary_item INT,"+
            "  price_usd DOUBLE,"+
            "  cogs_usd DOUBLE,"+
            "  created_at TIMESTAMP"+
            ");";
        String makeOrderItemRefundsString = 
            "CREATE TABLE IF NOT EXISTS order_item_refunds ("+
            "  order_item_refund_id INT AUTO_INCREMENT PRIMARY KEY, "+
            "  order_item_id INT REFERENCES order_items(order_item_id), "+
            "  order_id INT REFERENCES orders(order_id), "+
            "  refund_amount_usd DOUBLE, "+
            "  created_at TIMESTAMP"+
            ");";
        // TODO: Create the 7 PreparedStatements in a try-with-resources as auto-closables by calling conn.prepareStatement with the above strings
        // TODO:    Inside this try block:
        // TODO:        Call execute() on each PreparedStatement
        // TODO:    Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        try (PreparedStatement makeUsersStatement = conn.prepareStatement(makeUsersString);
            PreparedStatement makeProductStatement = conn.prepareStatement(makeProductsString);
            PreparedStatement makeWebsiteSessionStatement = conn.prepareStatement(makeWebsiteSessionsString);
            PreparedStatement makeWebsitePreviewStatement = conn.prepareStatement(makeWebsitePageviewsString);
            PreparedStatement makeOrderStatement = conn.prepareStatement(makeOrdersString);
            PreparedStatement makeOrderItemsStatement = conn.prepareStatement(makeOrderItemsString);
            PreparedStatement makeOrderItemRefundsStatement = conn.prepareStatement(makeOrderItemRefundsString);) {
            makeUsersStatement.execute();
            makeProductStatement.execute();
            makeWebsiteSessionStatement.execute();
            makeWebsitePreviewStatement.execute();
            makeOrderStatement.execute();
            makeOrderItemsStatement.execute();
            makeOrderItemRefundsStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static void processUsers(Connection conn) {
        // TODO: Create a PreparedStatement in a try-with-resources as auto-closable by calling conn.prepareStatement an insert statement
        // TODO: The sql should insert values into the "user_id" column of the "users" table
        // TODO:    Inside this try block: 
        // TODO:        Use a "for" loop from "i" equals 1 to 394318 (inclusive): 
        // TODO:            Set the first parameter marker of the PreparedStatement to i
        // TODO:            Call "addBatch" on the PreparedStatement
        // TODO:        Call "executeBatch" on the PreparedStatement
        // TODO:    Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO users (user_id) VALUES (?);")) {
            for (int i=1; i<=394318; i++) {
                statement.setInt(1, i);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static void processOrders(Connection conn) {
        // TODO: Call Order.csvTable.readTable() and store the result in a boolean variable
        // TODO: "if" the variable above is true:
        // TODO:    Create a PreparedStatement in a try-with-resources as auto-closable by calling conn.prepareStatement an insert statement
        // TODO:    The sql should insert values into the following columns of the "orders" table
        // TODO:            "order_id", "created_at", "website_session_id", "user_id", 
        // TODO:            "primary_product_id", "items_purchased", "price_usd", and "cogs_usd"
        // TODO:       Inside this try block: 
        // TODO:           Call "forEach" on Order.csvTable with a lambda that takes "order", and "entryNumber" as parameters: 
        // TODO:               Use the "order" object to get the fields of the table row and set them in the Parameter Markers
        // TODO:               Call "addBatch" on the PreparedStatement
        // TODO:               Whenever "entryNumber" is a multiple of "1000":
        // TODO:                    Call "executeBatch" on the PreparedStatement
        // TODO:           Call "executeBatch" on the PreparedStatement
        // TODO:       Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        Boolean bool = Order.csvTable.readTable();
        if (bool) {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO orders (order_id, created_at, website_session_id, user_id, primary_product_id, items_purchased, price_usd, cogs_usd) VALUES (?, ?, ?, ?, ?, ?, ?, ?);")) {
                Order.csvTable.forEach((order, entryNumber) -> {
                    statement.setInt(1, order.getOrderId());
                    statement.setTimestamp(2, order.getCreatedAt());
                    statement.setInt(3, order.getWebsiteSessionId());
                    statement.setInt(4, order.getUserId());
                    statement.setInt(5, order.getPrimaryProductId());
                    statement.setInt(6, order.getItemsPurchased());
                    statement.setDouble(7, order.getPriceUsd());
                    statement.setDouble(8, order.getCogsUsd());
                    statement.addBatch();
                    if (entryNumber % 1000 == 0) {
                        statement.executeBatch();
                    }
                });
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    public static void processProducts(Connection conn) {
        // TODO: Call Product.csvTable.readTable() and store the result in a boolean variable
        // TODO: "if" the variable above is true:
        // TODO:    Create a PreparedStatement in a try-with-resources as auto-closable by calling conn.prepareStatement an insert statement
        // TODO:    The sql should insert values into the following columns of the "products" table
        // TODO:            "product_id", "created_at", and "product_name"
        // TODO:       Inside this try block: 
        // TODO:           Call "forEach" on Product.csvTable with a lambda that takes "product", and "entryNumber" as parameters: 
        // TODO:               Use the "product" object to get the fields of the table row and set them in the Parameter Markers
        // TODO:               Call "addBatch" on the PreparedStatement
        // TODO:               Whenever "entryNumber" is a multiple of "1000":
        // TODO:                    Call "executeBatch" on the PreparedStatement
        // TODO:           Call "executeBatch" on the PreparedStatement
        // TODO:       Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        Boolean bool = Product.csvTable.readTable();
        if (bool) {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO products (product_id, created_at, product_name) VALUES (?, ?, ?);")) {
                Product.csvTable.forEach((product, entryNumber) -> {
                    statement.setInt(1, product.getProductId());
                    statement.setTimestamp(2, product.getCreatedAt());
                    statement.setString(3, product.getProductName());
                    statement.addBatch();
                    if (entryNumber % 1000 == 0) {
                        statement.executeBatch();
                    }
                });
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    public static void processOrderItems(Connection conn) {
        // TODO: Call OrderItem.csvTable.readTable() and store the result in a boolean variable
        // TODO: "if" the variable above is true:
        // TODO:    Create a PreparedStatement in a try-with-resources as auto-closable by calling conn.prepareStatement an insert statement
        // TODO:    The sql should insert values into the following columns of the "order_items" table
        // TODO:            "order_item_id", "order_id", "product_id", "is_primary_item", "price_usd", "cogs_usd", and "created_at"
        // TODO:       Inside this try block: 
        // TODO:           Call "forEach" on OrderItem.csvTable with a lambda that takes "orderItem", and "entryNumber" as parameters: 
        // TODO:               Use the "orderItem" object to get the fields of the table row and set them in the Parameter Markers
        // TODO:               Call "addBatch" on the PreparedStatement
        // TODO:               Whenever "entryNumber" is a multiple of "1000":
        // TODO:                    Call "executeBatch" on the PreparedStatement
        // TODO:           Call "executeBatch" on the PreparedStatement
        // TODO:       Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        Boolean bool = OrderItem.csvTable.readTable();
        if (bool) {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO order_items (order_item_id, order_id, product_id, is_primary_item, price_usd, cogs_usd, created_at) VALUES (?, ?, ?, ?, ?, ?, ?);")) {
                OrderItem.csvTable.forEach((orderItem, entryNumber) -> {
                    statement.setInt(1, orderItem.getOrderItemId());
                    statement.setInt(2, orderItem.getOrderId());
                    statement.setInt(3, orderItem.getProductId());
                    statement.setInt(4, orderItem.getIsPrimaryItem());
                    statement.setDouble(5, orderItem.getPriceUsd());
                    statement.setDouble(6, orderItem.getCogsUsd());
                    statement.setTimestamp(7, orderItem.getCreatedAt());
                    statement.addBatch();
                    if (entryNumber % 1000 == 0) {
                        statement.executeBatch();
                    }
                });
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    public static void processOrderItemRefunds(Connection conn) {
        // TODO: Call OrderItemRefund.csvTable.readTable() and store the result in a boolean variable
        // TODO: "if" the variable above is true:
        // TODO:    Create a PreparedStatement in a try-with-resources as auto-closable by calling conn.prepareStatement an insert statement
        // TODO:    The sql should insert values into the following columns of the "order_item_refunds" table
        // TODO:            "order_item_refund_id", "order_item_id", "order_id", "refund_amount_usd", and "created_at"
        // TODO:       Inside this try block: 
        // TODO:           Call "forEach" on OrderItemRefund.csvTable with a lambda that takes "orderItemRefund", and "entryNumber" as parameters: 
        // TODO:               Use the "orderItemRefund" object to get the fields of the table row and set them in the Parameter Markers
        // TODO:               Call "addBatch" on the PreparedStatement
        // TODO:               Whenever "entryNumber" is a multiple of "1000":
        // TODO:                    Call "executeBatch" on the PreparedStatement
        // TODO:           Call "executeBatch" on the PreparedStatement
        // TODO:       Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        Boolean bool = OrderItemRefund.csvTable.readTable();
        if (bool) {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO order_item_refunds (order_item_refund_id, order_item_id, order_id, refund_amount_usd, created_at) VALUES (?, ?, ?, ?, ?);")) {
                OrderItemRefund.csvTable.forEach((orderItemRefund, entryNumber) -> {
                    statement.setInt(1, orderItemRefund.getOrderItemRefundId());
                    statement.setInt(2, orderItemRefund.getOrderItemId());
                    statement.setInt(3, orderItemRefund.getOrderId());
                    statement.setDouble(4, orderItemRefund.getRefundAmountUsd());
                    statement.setTimestamp(5, orderItemRefund.getCreatedAt());
                    statement.addBatch();
                    if (entryNumber % 1000 == 0) {
                        statement.executeBatch();
                    }
                });
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    public static void processWebsiteSessions(Connection conn) {
        // TODO: Call WebsiteSession.csvTable.readTable() and store the result in a boolean variable
        // TODO: "if" the variable above is true:
        // TODO:    Create a PreparedStatement in a try-with-resources as auto-closable by calling conn.prepareStatement an insert statement
        // TODO:    The sql should insert values into the following columns of the "website_sessions" table
        // TODO:            "website_session_id", "user_id", "is_repeat_session", "utm_source", "utm_campaign",
        // TODO             "utm_content", "device_type", "http_referer", and "created_at"
        // TODO:       Inside this try block: 
        // TODO:           Call "forEach" on WebsiteSession.csvTable with a lambda that takes "websiteSession", and "entryNumber" as parameters: 
        // TODO:               Use the "websiteSession" object to get the fields of the table row and set them in the Parameter Markers
        // TODO:               Call "addBatch" on the PreparedStatement
        // TODO:               Whenever "entryNumber" is a multiple of "1000":
        // TODO:                    Call "executeBatch" on the PreparedStatement
        // TODO:           Call "executeBatch" on the PreparedStatement
        // TODO:       Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        Boolean bool = WebsiteSession.csvTable.readTable();
        if (bool) {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO website_sessions (website_session_id, user_id, is_repeat_session, utm_source, utm_campaign, utm_content, device_type, http_referer, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
                WebsiteSession.csvTable.forEach((websiteSession, entryNumber) -> {
                    statement.setInt(1, websiteSession.getWebsiteSessionId());
                    statement.setInt(2, websiteSession.getUserId());
                    statement.setInt(3, websiteSession.getIsRepeatSession());
                    statement.setString(4, websiteSession.getUtmSource());
                    statement.setString(5, websiteSession.getUtmCampaign());
                    statement.setString(6, websiteSession.getUtmContent());
                    statement.setString(7, websiteSession.getDeviceType());
                    statement.setString(8, websiteSession.getHttpReferer());
                    statement.setTimestamp(9, websiteSession.getCreatedAt());
                    statement.addBatch();
                    if (entryNumber % 1000 == 0) {
                        statement.executeBatch();
                    }
                });
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    public static void processWebsitePageviews(Connection conn) {
        // TODO: Call WebsitePageview.csvTable.readTable() and store the result in a boolean variable
        // TODO: "if" the variable above is true:
        // TODO:    Create a PreparedStatement in a try-with-resources as auto-closable by calling conn.prepareStatement an insert statement
        // TODO:    The sql should insert values into the following columns of the "website_pageviews" table
        // TODO:            "website_pageview_id", "website_session_id", "pageview_url", and "created_at"
        // TODO:       Inside this try block: 
        // TODO:           Call "forEach" on WebsitePageview.csvTable with a lambda that takes "websitePageview", and "entryNumber" as parameters: 
        // TODO:               Use the "websitePageview" object to get the fields of the table row and set them in the Parameter Markers
        // TODO:               Call "addBatch" on the PreparedStatement
        // TODO:               Whenever "entryNumber" is a multiple of "1000":
        // TODO:                    Call "executeBatch" on the PreparedStatement
        // TODO:           Call "executeBatch" on the PreparedStatement
        // TODO:       Catch SQLExceptions and, if one is raised, print its stack trace and exit the program with status code 2
        Boolean bool = WebsitePageview.csvTable.readTable();
        if (bool) {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO website_pageviews (website_pageview_id, website_session_id, pageview_url, created_at) VALUES (?, ?, ?, ?);")) {
                WebsitePageview.csvTable.forEach((websitePageview, entryNumber) -> {
                    statement.setInt(1, websitePageview.getWebsitePageviewId());
                    statement.setInt(2, websitePageview.getWebsiteSessionId());
                    statement.setString(3, websitePageview.getPageviewUrl());
                    statement.setTimestamp(4, websitePageview.getCreatedAt());
                    statement.addBatch();
                    if (entryNumber % 1000 == 0) {
                        statement.executeBatch();
                    }
                });
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    // TODO: Replace these strings with your databases URL, root username, and root user password
    private static final String URL = "jdbc:mariadb://localhost:3306/assignment1";
    private static final String USER = "root";
    private static final String PWORD = "password1";

    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PWORD)) {
            createTables(conn);
            processProducts(conn);
            processUsers(conn);
            processWebsiteSessions(conn);
            processWebsitePageviews(conn);
            processOrders(conn);
            processOrderItems(conn);
            processOrderItemRefunds(conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
