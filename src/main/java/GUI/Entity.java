package GUI;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Entity {
    private int x;
    private int y;
    private EntityStatus status;
    private EntityType type;
    private String imgPath;

    public Entity(int x, int y, EntityStatus status, EntityType type, String imgPath) {
        this.x = x;
        this.y = y;
        this.status = status;
        this.type = type;
        this.imgPath = imgPath;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public EntityType getType() {
        return type;
    }

    public Rectangle getEntityRectangle(){
        var rectangle = new Rectangle(x*Cell.Width,y*Cell.Height,Cell.Width,Cell.Height);
        Image img = new Image(imgPath, Cell.Width, Cell.Height, true, false);
        rectangle.setFill(new ImagePattern(img));
        return rectangle;
    }
}
