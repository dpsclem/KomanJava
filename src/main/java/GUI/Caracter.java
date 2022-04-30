package GUI;

import com.google.gson.annotations.Expose;
import javafx.scene.paint.Color;

public class Caracter {
    @Expose
    private int x;

    @Expose
    private int y;

    @Expose
    private String ImgPath = "file:resources/graphics/sprite/character.png";

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
}
