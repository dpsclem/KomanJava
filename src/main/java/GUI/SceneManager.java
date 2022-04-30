package GUI;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private Scene Scene;
    private List<Button> Buttons;

    public SceneManager(Group root, int width, int height, Color color, String title) {
        Scene = new Scene(root, width, height, color);
    }

    public Scene GetScene() {
        return Scene;
    }

    public List<Button> GetButtons(){
        var buttons = new ArrayList<Button>();
        buttons.add(GetResetMapButton());
        buttons.add(GetInventoryButton());
        return buttons;
    }

    private Button GetResetMapButton() {
        Button resetMapBtn = new Button();
        resetMapBtn.setText("Reset Map");
        resetMapBtn.setLayoutX(1220);
        resetMapBtn.setLayoutY(50);
        resetMapBtn.setPrefSize(80, 30);
        return resetMapBtn;
    }

    private Button GetInventoryButton() {
        Button button = new Button();

        Image img = new Image("file:resources/graphics/bag.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        button.setGraphic(view);

        button.setLayoutX(1220);
        button.setLayoutY(100);
        button.setPrefSize(50, 50);
        return button;
    }
}
