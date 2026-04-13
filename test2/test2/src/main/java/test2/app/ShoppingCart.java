package test2.app;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import test2.model.Cart;
import test2.model.Item;

//! Challenge level: Easy
// TODO: Name this the "cartBean" and make it session scoped
@Named("cartBean")
@SessionScoped
public class ShoppingCart implements Serializable{
    @Inject private User user;
    private Cart cart;
    private Client client;
    private WebTarget base;

    //! Challenge level: Medium
    // TODO: Make this method the post construct method
    @PostConstruct
    public void setup() {
        // TODO: set client to a new client using the ClientBuilder class
        // TODO: set base to the target of "http://localhost:8080/test2/service"
        client = ClientBuilder.newClient();
        base = client.target("http://localhost:8080/test2/service");
    }

    //! Challenge level: Easy
    // TODO: Make this method the pre destroy method
    @PreDestroy
    public void tearDown() {
        // TODO: close the client
        client.close();
    }

    //! Challenge level: Hard
    public void getCartByUserId(){
        System.out.println("ShoppingCart.getCartByUserId: Getting cart");
        try{
            WebTarget target = null;
            // TODO: using the base WebTarget, and the path method, set target to a WebTarget for the following URI
            // TODO:    "http://localhost:8080/test2/service/cart/getOrMakeByUser/" + user.getUserId()
            target = base.path("cart/getOrMakeByUser/" + user.getUserId());
            System.out.println("ShoppingCart.getCartByUserId: " + target.getUri().toURL().toString());
            // TODO: using:
            // TODO:    target, 
            // TODO:    the request method of WebTarget with a media type of application/json
            // TODO:    the get method of the Builder with Cart.class as the response type, 
            // TODO: set cart to the result running this client request
            cart = target.request(MediaType.APPLICATION_JSON).get(Cart.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //! Challenge level: Hard
    public void addItem(String brand, String name, String size){
        System.out.println("ShoppingCart.addItem: Adding item: " + brand + ":" + name + ":" + size);
        if(cart == null)
            getCartByUserId();
        System.out.println("ShoppingCart.addItem: " + cart == null ? "cart was null" : "cart was not null");
        System.out.println("ShoppingCart.addItem: Getting item");
        Item item = null;
        // TODO: using the base WebTarget, and the path method, create a WebTarget for the following URI
        // TODO:    "http://localhost:8080/test2/service/item/" + brand + "/" + name + "/" + size
        // TODO: and using:
        // TODO:    the request method of WebTarget with a media type of application/json
        // TODO:    the get method of the Builder with Item.class as the response type, 
        // TODO: set item to the result running this client request
        WebTarget target = base.path("item/" + brand + "/" + name + "/" + size);
        item = target.request(MediaType.APPLICATION_JSON).get(Item.class);
        System.out.println("ShoppingCart.addItem: " + item == null ? "item was null" : "item was not null");
        if (item == null) return; 
        System.out.println("ShoppingCart.addItem: Getting new cart");
        // TODO: using the base WebTarget, and the path method, create a WebTarget for the following URI
        // TODO:    "http://localhost:8080/test2/service/cart/add/" + cart.getId() + "/" + item.getId()
        // TODO: and using:
        // TODO:    the request method of WebTarget with a media type of application/json
        // TODO:    the get method of the Builder with Cart.class as the response type, 
        // TODO: set cart to the result running this client request
        WebTarget addTarget = base.path("cart/add/" + cart.getId() + "/" + item.getId());
        cart = addTarget.request(MediaType.APPLICATION_JSON).get(Cart.class);
        System.out.println("ShoppingCart.addItem: " + cart == null ? "cart was null" : "cart was not null");
    }

    public void add2LPepsi(){
        addItem("Pepsi", "Cola", "2l");
    }

    public void add6X710MlPepsi(){
        addItem("Pepsi", "Cola", "6x710ml");
    }

    public void add12X355MlPepsi(){
        addItem("Pepsi", "Cola", "12x355ml");
    }

    public void add591MlPepsi(){
        addItem("Pepsi", "Cola", "591ml");
    }

    public void add2LCoke(){
        addItem("Coke", "Cola", "2l");
    }

    public void add6X710MlCoke(){
        addItem("Coke", "Cola", "6x710ml");
    }

    public void add12X355MlCoke(){
        addItem("Coke", "Cola", "12x355ml");
    }

    public void add591MlCoke(){
        addItem("Coke", "Cola", "591ml");
    }

    public void add2LBig8(){
        addItem("Big 8", "Cola", "2l");
    }

    public void add12X355MlBig8(){
        addItem("Big 8", "Cola", "12x355ml");
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return this.cart;
    }
}