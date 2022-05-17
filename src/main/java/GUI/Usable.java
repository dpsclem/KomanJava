package GUI;

public class Usable {
    private int usesNumber;
    private boolean isPassive; //If usable can be used by player by clicking or not.
    private UsableType usableType;
    private ItemEffects itemEffect; //Effect that will be applied to the player
    private int usablePower;

    public int getUses(){
        return this.usesNumber;
    }

    public boolean getIsPassive(){
        return this.isPassive;
    }

    public int getUsesNumber() {
        return usesNumber;
    }

    public UsableType getUsableType(){
        return this.usableType;
    }


    public void decreaseUses(){
        this.usesNumber -=1;
    }

    public int getUsablePower(){
        return this.usablePower;
    }
}
