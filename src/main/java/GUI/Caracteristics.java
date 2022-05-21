package GUI;

public class Caracteristics {


    private int attack;
    private int armor;
    private int maxHp;


    // MUST BE SET TO 0 IN CONSTRUCTOR IF ITEM
    // MUST BE = TO MAXHP IN CONSTRUCTOR IF MONSTER OR PLAYER
    private int currentHP;

    public Caracteristics(int attack, int armor, int maxhp, int currentHP) {
        this.attack = attack;
        this.armor = armor;
        this.maxHp = maxhp;
        this.currentHP = currentHP;
    }

    public void addCaracteristics(Caracteristics carac) {
        this.attack += carac.getAttack();
        this.armor += carac.getArmor();
        this.maxHp += carac.getMaxHp();
    }

    public void substractCaracteristics(Caracteristics carac) {
        this.attack -= carac.getAttack();
        this.armor -= carac.getArmor();
        this.maxHp -= carac.getMaxHp();
    }

    public int getAttack() {
        return attack;
    }

    public int getArmor() {
        return armor;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    //Current HP setters and getters
    public void setHp(int hp) {
        this.currentHP = hp;
    }

    public int getCurrentHP() {
        return this.currentHP;
    }
}
