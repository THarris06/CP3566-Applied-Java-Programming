package test2.model;

import java.io.Serializable;

public class Item implements Serializable{
    private long id;
    private String name;
    private String brand;
    private String size;
    private long unitCost;
    public static Item makeItem(long id, String name, String brand, String size, long unitCost){
        Item newItem = new Item();
        newItem.setId(id);
        newItem.setName(name);
        newItem.setBrand(brand);
        newItem.setSize(size);
        newItem.setUnitCost(unitCost);
        return newItem;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public long getUnitCost() {
        return unitCost;
    }
    public void setUnitCost(long unitCost) {
        this.unitCost = unitCost;
    }
    @Override
    public String toString() {
        return "Item ["
                + "id=" + id 
                + ", name=" + name 
                + ", brand=" + brand 
                + ", size=" + size 
                + ", unitCost=" + unitCost
                + "]";
    }
    
}
