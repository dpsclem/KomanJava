package GUI;

import com.google.gson.annotations.Expose;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Caracter {
    @Expose
    private int x;

    @Expose
    private int y;

    @Expose
    private String ImgPath = "file:resources/graphics/sprite/character.png";

    @Expose
    private Caracteristics caracteristics;

    public List<Item> getItems() {
        return items;
    }


    public Caracter(int x, int y) {
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
    private List<Item> items = new ArrayList<Item>();

    public Caracteristics getCaracteristics() {
        return caracteristics;
    }

    public void setCaracteristics(Caracteristics caracteristics) {
        this.caracteristics = caracteristics;
    }

    public void EquipItem(Equipment item){
        caracteristics.AddCaracteristics(item.getCaracteristics());
        item.setEquiped(true);
    }

    public void UnequipItem(Equipment item){
        caracteristics.SubstractCaracteristics(item.getCaracteristics());
        item.setEquiped(false);
    }
}
