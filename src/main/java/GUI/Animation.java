package GUI;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Animation {
    private double length;
    private String imagePath;
    private Group group;
    private Group animGroup;
    private int sizeX;
    private int sizeY;
    private int posX;
    private int posY;


    public Animation(double length,String imagePath, Group group, int sizeX, int sizeY, int posX, int posY) {
        this.length = length;
        this.imagePath = imagePath;
        this.group = group;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.posX = posX;
        this.posY = posY;
    }

    public double getAnimationLength(){
        return this.length;
    }

    public void playAnimation(){
        animGroup = new Group();

        var rectangle = new Rectangle(posX,posY,sizeX,sizeY);
        Image img = new Image(imagePath,sizeX, sizeY, true, false);
        rectangle.setFill(new ImagePattern(img));
        animGroup.getChildren().add(rectangle);
        group.getChildren().add(animGroup);

    }

    public void removeAnimation(){
        group.getChildren().remove(this.animGroup);
    }
}