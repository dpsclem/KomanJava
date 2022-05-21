package Entity;

import Item.Item;

import java.util.ArrayList;
import java.util.List;

public class Chest extends Entity{


    public List<Item> items = new ArrayList<Item>();

    public Chest(int x, int y, EntityStatus status, EntityType type, List<Item> items,String imgPath, TrapType trap) {
        super(x, y, status, type, trap, imgPath);
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItemInChest(Item item){
        items.add(item);
    }

    public void removeItemInChest(Item item){
        items.remove(item);
    }
}
