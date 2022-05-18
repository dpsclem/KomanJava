package GUI;

import Item.Item;
import com.google.gson.annotations.Expose;
import Item.*;
import java.util.ArrayList;
import java.util.List;

public class Character {
    @Expose
    private int x;

    @Expose
    private int y;

    @Expose
    private String ImgPath = "file:resources/graphics/sprite/character.png";

    @Expose
    private Caracteristics caracteristics;

    @Expose
    private int money = 0;

    public List<Item> getItems() {
        return items;
    }

    public boolean hasItem(Item item){
        return items.contains(item);
    }


    public Character(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    private List<Item> items = new ArrayList<Item>();

    public Caracteristics getCaracteristics() {
        return caracteristics;
    }

    public void setCaracteristics(Caracteristics caracteristics) {
        this.caracteristics = caracteristics;
    }

    public void equipItem(Equipment item){
        caracteristics.addCaracteristics(item.getCaracteristics());
        item.setEquiped(true);
    }

    public void unequipItem(Equipment item){
        caracteristics.substractCaracteristics(item.getCaracteristics());
        item.setEquiped(false);
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int qty) {
        this.money += qty ;
    }

    public void takeMoney(int qty) {
        this.money -= qty ;
    }

    public void sellItem(Item item) {
        addMoney(item.getPrice());
        removeItem(item);
    }
}
