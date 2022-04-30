package GUI;

import com.google.gson.annotations.Expose;
import javafx.scene.paint.Color;

public class Caracter {
    @Expose
    private int x;

    @Expose
    private int y;

    @Expose
    private Color color;

    public Caracter(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
