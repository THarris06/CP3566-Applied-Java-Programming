package test2.model;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable{
    private long id;
    private List<Item> items;
    private long subTotal;
    public static Cart makeCart(
        long id,
        List<Item> items,
        long subTotal
    ){
        Cart newCart = new Cart();
        newCart.setId(id);
        newCart.setItems(items);
        newCart.setSubTotal(subTotal);
        return newCart;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public long getSubTotal() {
        return subTotal;
    }
    public void setSubTotal(long subTotal) {
        this.subTotal = subTotal;
    }
}
