package SceneManager;

import GUI.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class SceneInterface {

    public List<Node> getInterface(Group root, Map map) {
        var nodes = new ArrayList<Node>();

        Image heartImage = new Image("file:resources/graphics/interface/heart.png", 50, 50, true, false);
        Image attackImage = new Image("file:resources/graphics/interface/attack.png", 50, 50, true, false);
        Image armorImage = new Image("file:resources/graphics/interface/armor.png", 50, 50, true, false);

        Canvas caracteristicsCanvas = new Canvas(700, 70);
        caracteristicsCanvas.setLayoutX(50);
        caracteristicsCanvas.setLayoutY(750);

        GraphicsContext gc = caracteristicsCanvas.getGraphicsContext2D();
        gc.setFont(new Font("Arial", 50));

        gc.setFill(new ImagePattern(heartImage));
        gc.fillRect(0, 0, 50, 50);

        gc.setFill(Color.BLACK);
        gc.fillText("" + map.getCharacter().getCaracteristics().getHp(), 50, 50);

        gc.setFill(new ImagePattern(attackImage));
        gc.fillRect(250, 0, 50, 50);

        gc.setFill(Color.BLACK);
        gc.fillText("" + map.getCharacter().getCaracteristics().getAttack(), 300, 50);

        gc.setFill(new ImagePattern(armorImage));
        gc.fillRect(500, 0, 50, 50);

        gc.setFill(Color.BLACK);
        gc.fillText("" + map.getCharacter().getCaracteristics().getArmor(), 550, 50);

        nodes.add(caracteristicsCanvas);
        return nodes;

    }
}
