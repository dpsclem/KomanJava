package Entity;

import GUI.Character;
import Item.Item;

import java.util.ArrayList;
import java.util.List;

public class Merchant extends Entity {
    private List<Item> items=new ArrayList<Item>();
    private String dialog="Bonjour, koman java? De quoi avez-vous besoin ?";


    public Merchant(int x, int y, List<Item> items, EntityStatus status, EntityType type, String imgPath, TrapType trap) {
        super(x, y, status, type, trap, imgPath);
        this.items=items;

    }

    public List<Item> getItems() {
        return items;
    }
    public void removeItem(Item item){
        items.remove(item);
    }
    public void addItem(Item item){
        items.add(item);
    }
    public String getDialog() {
        return dialog;
    }


    public void buyItem(Item item, Character character) {
        if (character.getMoney()>=item.getPrice()){ //si le personnage a assez d'argent
            character.takeMoney(item.getPrice()); //on enlève l'argent au personnage
            character.addItem(item); //on ajoute l'item au personnage
            removeItem(item); //on enlève l'item du marchand
        }
    }
}
