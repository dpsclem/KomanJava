package GUI;

import javafx.print.Printer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private Scene Scene;
    private List<Button> Buttons;
    private Group Root;
    private Map Map;

    private boolean IsInventoryOpen = false;

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
        addEntities();

        Scene.addEventFilter(KeyEvent.KEY_PRESSED, e->{
            Root.getChildren().clear();
            try {
                switch (e.getCode()) {
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
            }
            finally {
                addEntities();
                AddButtons();
            }
        });
    }

    private void addEntities()
    {
        var entities = Map.getEntities();
        for(var entity : entities)
        {
            Root.getChildren().add(entity.getEntityRectangle());
        }
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
            addEntities();
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
            System.out.println("Open inventory");

            Root.getChildren().clear();

            FillSceneWithMap(Root, Map);
            AddButtons();
            addEntities();
            if(!IsInventoryOpen) {
                IsInventoryOpen = true;

                Canvas inventoryCanvas = new Canvas(830, 500);
                inventoryCanvas.setLayoutX(185);
                inventoryCanvas.setLayoutY(110);
                GraphicsContext gc = inventoryCanvas.getGraphicsContext2D();
                gc.setFill(Color.LIGHTCORAL);
                gc.fillRect(0, 0, 830, 500);

                //PETE MOI LE CUL MAT
                var items = Map.getCaracter().getItems();
                var itemsIterator = items.iterator();
                for (int i = 0; i < 7; i++) {
                    for (int j =0; j < 7; j++) {
                        System.out.println(items.size());
                        if (itemsIterator.hasNext()) {
                            var item = itemsIterator.next();
                            var itemImage = new Image(item.getImagePath(), 70, 70, true, false);
                            gc.setFill(new ImagePattern(itemImage));
                            gc.fillRect((j *70) + (j*40) + 30, (i * 70)+(i*40) + 30, 70, 70);
                        }
                    }
                }
                Root.getChildren().add(inventoryCanvas);
            }
            else IsInventoryOpen = false;
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

        for (Rectangle currentRectangle: randomMap.GetItemRectangle()) {
            root.getChildren().add(currentRectangle);
        }
    }
}
