package Entity;

import GUI.Caracteristics;
import Item.Item;

import java.util.ArrayList;
import java.util.List;

public class Monster extends Entity{
    public Caracteristics getCaracteristics() {
        return caracteristics;
    }


    private Caracteristics caracteristics;
    private List<Item> dropList = new ArrayList<Item>();

    public Monster(int x, int y, EntityStatus status, EntityType type, String imgPath,Caracteristics caracteristics,List<Item> dropList) {
        super(x, y, status, type, imgPath);
        this.caracteristics = caracteristics;
        this.dropList = dropList;

    }

    public void takeDamages(int damages){

        double calculated = 1+damages/(1 + 0.4*this.caracteristics.getArmor());
        System.out.println("Damage calculated = "+calculated);
        this.caracteristics.setHp(this.caracteristics.getCurrentHP() - (int) calculated);
    }

    //Puts items in player's inventory
    public List<Item> getDropList(){
        return this.dropList;
    }
}
