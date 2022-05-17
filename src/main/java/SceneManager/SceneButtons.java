package SceneManager;

import GUI.*;
import GUI.Character;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class SceneButtons {

    private boolean IsInventoryOpen = false;


    public List<Node> getButtons(Group root, Map map, SceneManager sceneManager){
        var buttons = new ArrayList<Node>();
        buttons.add(getResetMapButton(root, map, sceneManager));
        buttons.add(getInventoryButton(root, map, sceneManager));
        buttons.addAll(getNearInteractions(root, map, sceneManager));
        return buttons;
    }

    public boolean isInventoryOpen() {
        return IsInventoryOpen;
    }

    private Button getResetMapButton(Group root, Map map, SceneManager sceneManager){
        Button resetMapBtn = new Button();
        resetMapBtn.setText("Reset Map");
        resetMapBtn.setLayoutX(1220);
        resetMapBtn.setLayoutY(50);
        resetMapBtn.setPrefSize(80, 30);

        resetMapBtn.setOnAction(event -> {
            root.getChildren().clear();
            map.UpdateWithRandomMap();
            var resetCaracter = new Character(1,1);
            resetCaracter.setCaracteristics(new Caracteristics(20,5,5));
            map.setCaracter(resetCaracter);
            sceneManager.addAll();
        });
        return resetMapBtn;
    }

    private Button getInventoryButton(Group root, Map map, SceneManager sceneManager) {
        Button button = new Button();

        button.setLayoutX(1220);
        button.setLayoutY(100);
        button.setPrefSize(40, 50);

        Image img = new Image("file:resources/graphics/bag.png", 70, 70, true, false);
        ImageView view = new ImageView(img);
        button.setGraphic(view);

        button.setOnAction(event -> {
            System.out.println("Open inventory");

            root.getChildren().clear();
            sceneManager.addAll();
            if(!IsInventoryOpen) {
                IsInventoryOpen = true;

                Canvas inventoryCanvas = new Canvas(830, 500);
                inventoryCanvas.setLayoutX(185);
                inventoryCanvas.setLayoutY(110);
                GraphicsContext gc = inventoryCanvas.getGraphicsContext2D();
                gc.setFill(Color.LIGHTCORAL);
                gc.fillRect(0, 0, 830, 500);
                root.getChildren().add(inventoryCanvas);

                //PETE MOI LE CUL MAT
                var items = map.getCharacter().getItems();
                var itemsIterator = items.iterator();
                for (int i = 0; i < 7; i++) {
                    for (int j =0; j < 7; j++) {
                        if (itemsIterator.hasNext()) {
                            var item = itemsIterator.next();
                            var itemImage = new Image(item.getImagePath(), 70, 70, true, false);

                            Canvas currentCanva = new Canvas(70, 70);
                            currentCanva.setLayoutX(185 + (j *70) + (j*40) + 30);
                            currentCanva.setLayoutY(110 + (i * 70) + (i*40) + 30);

                            GraphicsContext currentGc = currentCanva.getGraphicsContext2D();
                            if(item instanceof Equipment){
                                if(((Equipment) item).isEquiped()) {
                                    currentGc.setFill(Color.RED);
                                    currentGc.fillRect(0, 0, 70, 70);

                                    Tooltip.install(currentCanva, new Tooltip("HP: " + ((Equipment) item).getCaracteristics().getHp() + "\n"
                                            + "Attack: " + ((Equipment) item).getCaracteristics().getAttack()
                                            + "Armor: " + ((Equipment) item).getCaracteristics().getArmor() + "\n"));
                                }
                                Canvas hoverInformations = new Canvas(100, 150);
                                hoverInformations.setLayoutX( currentCanva.getLayoutX() + currentCanva.getWidth() + 20);
                                hoverInformations.setLayoutY(currentCanva.getLayoutY() - 20);

                                GraphicsContext hoverGC = hoverInformations.getGraphicsContext2D();
                                hoverGC.setFill(Color.LIGHTGRAY);
                                hoverGC.fillRect(0, 0, 100, 150);


                                hoverGC.setFont(new Font("Arial", 30));

                                Image heartImage = new Image("file:resources/graphics/interface/heart.png", 20, 20, true, false);
                                Image attackImage = new Image("file:resources/graphics/interface/attack.png", 20, 20, true, false);
                                Image armorImage = new Image("file:resources/graphics/interface/armor.png", 20, 20, true, false);

                                int marginWidth = 10;
                                int marginHeight = 20;

                                hoverGC.setFill(new ImagePattern(heartImage));
                                hoverGC.fillRect(marginWidth, marginHeight, 30, 30);

                                hoverGC.setFill(Color.BLACK);
                                hoverGC.fillText("" + ((Equipment)item).getCaracteristics().getHp(), marginWidth + 50, marginHeight + 20);

                                hoverGC.setFill(new ImagePattern(attackImage));
                                hoverGC.fillRect(marginWidth, marginHeight + 40, 30, 30);

                                hoverGC.setFill(Color.BLACK);
                                hoverGC.fillText("" + ((Equipment)item).getCaracteristics().getAttack(), marginWidth + 50, marginHeight + 60);

                                hoverGC.setFill(new ImagePattern(armorImage));
                                hoverGC.fillRect(marginWidth, marginHeight + 80, 30, 30);

                                hoverGC.setFill(Color.BLACK);
                                hoverGC.fillText("" + ((Equipment)item).getCaracteristics().getArmor(), marginWidth + 50, marginHeight + 100);

                                currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                                    if(newValue)
                                    {
                                        root.getChildren().add(hoverInformations);
                                    }
                                    else
                                    {
                                        root.getChildren().remove(hoverInformations);
                                    }

                                });
                            }
                            currentGc.setFill(new ImagePattern(itemImage));
                            currentGc.fillRect(0, 0, 70, 70);

                            currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                                System.out.println("Clicked on item");
                                if(item instanceof Equipment){
                                    if(!((Equipment) item).isEquiped()){
                                        map.getCharacter().equipItem((Equipment) item);

                                        currentGc.setFill(Color.RED);
                                        currentGc.fillRect(0, 0, 70, 70);
                                        currentGc.setFill(new ImagePattern(itemImage));
                                        currentGc.fillRect(0, 0, 70, 70);
                                    }
                                    else
                                    {
                                        map.getCharacter().unequipItem((Equipment) item);
                                        currentGc.setFill(Color.LIGHTCORAL);
                                        currentGc.fillRect(0, 0, 70, 70);
                                        currentGc.setFill(new ImagePattern(itemImage));
                                        currentGc.fillRect(0, 0, 70, 70);
                                    }
                                }
                                else
                                {
                                    map.getCharacter().getItems().remove(item);
                                    root.getChildren().remove(currentCanva);
                                }
                            });
                            root.getChildren().add(currentCanva);
                        }
                    }
                }
            }
            else IsInventoryOpen = false;
        });
        return button;
    }
    
    private List<Button> getNearInteractions(Group root, Map map, SceneManager sceneManager)
    {
        List<Button> buttons = new ArrayList<>();

        var characterX = map.getCharacter().getX();
        var characterY = map.getCharacter().getY();

        var nearEntities = map.getNearEntities(characterX, characterY);

        for (var entity : nearEntities) {
            Button button = new Button();

            button.setLayoutX(1220);
            button.setLayoutY(180 + (nearEntities.indexOf(entity) * 50) + (nearEntities.indexOf(entity) * 10));
            button.setPrefSize(140, 50);

            button.setText("Interact with " + entity.getType());

            button.setOnAction(event -> {
                root.getChildren().clear();
                entity.interact(map, entity);
                sceneManager.addAll();
            });

            buttons.add(button);
        }
        return buttons;
    }
}
