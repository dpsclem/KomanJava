package GUI;

public class Caracteristics {
    private int attack;
    private int armor;
    private int hp;

    public Caracteristics(int attack, int armor, int hp) {
        this.attack = attack;
        this.armor = armor;
        this.hp = hp;
    }

    public void addCaracteristics(Caracteristics carac) {
        this.attack += carac.getAttack();
        this.armor += carac.getArmor();
        this.hp += carac.getHp();
    }

    public void substractCaracteristics(Caracteristics carac) {
        this.attack -= carac.getAttack();
        this.armor -= carac.getArmor();
        this.hp -= carac.getHp();
    }

    public int getAttack() {
        return attack;
    }

    public int getArmor() {
        return armor;
    }

    public int getHp() {
        return hp;
    }
}
