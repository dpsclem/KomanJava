package SceneManager;

import GUI.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class SceneManager {

    private Scene Scene;
    private List<Button> Buttons;
    private Group Root;
    private GUI.Map Map;

    private SceneButtons sceneButtons;
    private SceneInterface sceneInterface;
    private SceneEntities sceneEntities;

    public SceneManager(Group root, int width, int height, Color color) {
        Scene = new Scene(root, width, height, color);
        Root = root;

        sceneButtons = new SceneButtons();
        sceneInterface = new SceneInterface();
        sceneEntities = new SceneEntities();
    }

    public Scene GetScene() {
        return Scene;
    }

    public void addAll(){
        FillSceneWithMap(Root, Map);
        addButtons();
        addEntities();
        addInterface();
    }

    public void Initialize(Map map) {
        Map = map;
        addAll();

        Scene.addEventFilter(KeyEvent.KEY_PRESSED, e->{
            if (sceneButtons.isInventoryOpen()) { return; }
            Root.getChildren().clear();
            try {
                switch (e.getCode()) {
                    case LEFT:
                        Map.MoveCaracterLeft();
                        System.out.println("Go left");
                        break;
                    case RIGHT:
                        Map.MoveCaracterRight();
                        System.out.println("Go right");
                        break;
                    case UP:
                        Map.MoveCaracterUp();
                        System.out.println("Go up");
                        break;
                    case DOWN:
                        Map.MoveCaracterDown();
                        System.out.println("Go down");
                        break;
                    default:
                        System.out.println("Other click detected");
                        break;
                }
            }
            finally {
                addAll();
            }
        });
    }



    private void addEntities()
    {
        Root.getChildren().addAll(sceneEntities.getEntities(Root, Map));
    }

    private void addButtons(){
        Root.getChildren().addAll(sceneButtons.getButtons(Root, Map, this));
    }

    private void addInterface(){
        Root.getChildren().addAll(sceneInterface.getInterface(Root, Map));
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

        for (Rectangle currentRectangle: randomMap.GetItemRectangle()) {
            root.getChildren().add(currentRectangle);
        }
    }
}
