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
    private Group Root;
    private Map Map;

    public SceneManager(Group root, int width, int height, Color color) {
        Scene = new Scene(root, width, height, color);
        Root = root;
    }

    public Scene GetScene() {
        return Scene;
    }

    public void Initialize(Map map) {
        Map = map;
        FillSceneWithMap(Root, Map);
        AddButtons();

        Scene.addEventFilter(KeyEvent.KEY_PRESSED, e->{
            Root.getChildren().clear();
            AddButtons();
            switch(e.getCode()) {
                case LEFT:
                    Map.MoveCaracterLeft();
                    FillSceneWithMap(Root, Map);
                    System.out.println("Go left");
                    break;
                case RIGHT:
                    Map.MoveCaracterRight();
                    FillSceneWithMap(Root, Map);
                    System.out.println("Go right");
                    break;
                case UP:
                    Map.MoveCaracterUp();
                    FillSceneWithMap(Root, Map);
                    System.out.println("Go up");
                    break;
                case DOWN:
                    Map.MoveCaracterDown();
                    FillSceneWithMap(Root, Map);
                    System.out.println("Go down");
                    break;
                default:
                    FillSceneWithMap(Root, Map);
                    System.out.println("Other click detected");
                    break;
            }
        });
    }

    private void AddButtons(){
        Buttons = GetButtons();
        Root.getChildren().addAll(Buttons);
    }

    private List<Button> GetButtons(){
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

        resetMapBtn.setOnAction(event -> {
            Root.getChildren().clear();
            Map.UpdateWithRandomMap();
            var resetCaracter = new Caracter(1,1);
            Map.SetCaracter(resetCaracter);
            FillSceneWithMap(Root, Map);
            AddButtons();
        });
        return resetMapBtn;
    }

    private Button GetInventoryButton() {
        Button button = new Button();

        button.setLayoutX(1220);
        button.setLayoutY(100);
        button.setPrefSize(40, 50);

        Image img = new Image("file:resources/graphics/bag.png", 70, 70, true, false);
        ImageView view = new ImageView(img);
        button.setGraphic(view);

        button.setOnAction(event -> {
            Root.getChildren().clear();
            Map.UpdatePlayerPosition(new Coordinates(1,1));
            FillSceneWithMap(Root, Map);
            AddButtons();
        });
        return button;
    }

    private void FillSceneWithMap(Group root, Map randomMap) {
        for (int i = 0; i < randomMap.GetTableWidth(); i++) {
            for (int j = 0; j < randomMap.GetTableHeight(); j++) {
                Cell cell = randomMap.GetCellFromCoordinate(i, j);
                var rectangle = cell.GetRectangle();
                root.getChildren().add(rectangle);
            }
        }
        Cell cell = randomMap.GetCaracterCell();
        var rectangle = cell.GetRectangle();
        root.getChildren().add(rectangle);
    }
}
