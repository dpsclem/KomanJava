package Item;

import GUI.Caracteristics;

public class Equipment extends Item{
    private Caracteristics caracteristics;
    private EquipmentType type;
    private Boolean IsEquiped = false;

    public Equipment(String name, int price, EquipmentType type, Caracteristics caracteristics, String imgPath){
        super(name, price, imgPath);
        this.caracteristics = caracteristics;
        this.type = type;
    }

    public Caracteristics getCaracteristics() {
        return caracteristics;
    }

    public Boolean isEquiped() {
        return IsEquiped;
    }

    public void setEquiped(Boolean equiped) {
        IsEquiped = equiped;
    }
}
