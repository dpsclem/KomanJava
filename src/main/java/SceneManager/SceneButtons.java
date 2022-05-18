package SceneManager;

import CombatLogic.CombatManager;

import Entity.Chest;
import Entity.EntityType;
import Entity.Monster;
import GUI.*;
import GUI.Character;
import Item.Equipment;
import Item.EquipmentType;
import Item.Usable;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
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
import java.util.concurrent.CountedCompleter;

public class SceneButtons {

    private boolean IsInventoryOpen = false;


    public List<Node> getButtons(Group root, Map map, SceneManager sceneManager) {
        var buttons = new ArrayList<Node>();
        buttons.add(getResetMapButton(root, map, sceneManager));
        buttons.add(getInventoryButton(root, map, sceneManager));
        buttons.addAll(getNearInteractions(root, map, sceneManager));
        return buttons;
    }

    public boolean isInventoryOpen() {
        return IsInventoryOpen;
    }

    private Button getResetMapButton(Group root, Map map, SceneManager sceneManager) {
        Button resetMapBtn = new Button();
        resetMapBtn.setText("Reset Map");
        resetMapBtn.setLayoutX(1220);
        resetMapBtn.setLayoutY(50);
        resetMapBtn.setPrefSize(80, 30);

        resetMapBtn.setOnAction(event -> {
            root.getChildren().clear();
            map.UpdateWithRandomMap();
            var resetCaracter = new Character(1, 1);
            resetCaracter.setCaracteristics(new Caracteristics(20, 5, 5));
            var shield = new Equipment("Shield", 8, EquipmentType.SHIELD, new Caracteristics(0, 20, 0), "file:resources/graphics/sprite/equipements/shield1.png");
            resetCaracter.addItem(shield);
            var chestplate = new Equipment("Chestplate", 10, EquipmentType.CHESTPLATE, new Caracteristics(0, 10, 10), "file:resources/graphics/sprite/equipements/chestplate1.png");
            resetCaracter.addItem(chestplate);
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
            if (!IsInventoryOpen) {
                IsInventoryOpen = true;

                Canvas inventoryCanvas = new Canvas(830, 500);
                inventoryCanvas.setLayoutX(185);
                inventoryCanvas.setLayoutY(110);
                GraphicsContext gc = inventoryCanvas.getGraphicsContext2D();
                gc.setFill(Color.DARKBLUE);
                gc.fillRect(0, 0, 830, 500);
                root.getChildren().add(inventoryCanvas);

                var items = map.getCharacter().getItems();
                var itemsIterator = items.iterator();
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (itemsIterator.hasNext()) {
                            var item = itemsIterator.next();
                            var itemImage = new Image(item.getImagePath(), 70, 70, true, false);

                            Canvas currentCanva = new Canvas(70, 70);
                            currentCanva.setLayoutX(185 + (j * 70) + (j * 40) + 30);
                            currentCanva.setLayoutY(110 + (i * 70) + (i * 40) + 30);

                            GraphicsContext currentGc = currentCanva.getGraphicsContext2D();
                            if (item instanceof Equipment) {
                                if (((Equipment) item).isEquiped()) {
                                    currentGc.setFill(Color.RED);
                                    currentGc.fillRect(0, 0, 70, 70);
                                }
                                Canvas hoverInformations = new Canvas(100, 180);
                                hoverInformations.setLayoutX(currentCanva.getLayoutX() + currentCanva.getWidth() + 20);
                                hoverInformations.setLayoutY(currentCanva.getLayoutY() - 20);

                                AddHoverDisplay((Equipment) item, hoverInformations);

                                currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                                    if (newValue) root.getChildren().add(hoverInformations);
                                    else root.getChildren().remove(hoverInformations);
                                });
                            }
                            currentGc.setFill(new ImagePattern(itemImage));
                            currentGc.fillRect(0, 0, 70, 70);

                            currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                                System.out.println("Clicked on item");
                                if (item instanceof Equipment) {
                                    if (!((Equipment) item).isEquiped()) {
                                        map.getCharacter().equipItem((Equipment) item);

                                        currentGc.setFill(Color.RED);
                                        currentGc.fillRect(0, 0, 70, 70);
                                        currentGc.setFill(new ImagePattern(itemImage));
                                        currentGc.fillRect(0, 0, 70, 70);
                                    } else {
                                        map.getCharacter().unequipItem((Equipment) item);
                                        currentGc.setFill(Color.LIGHTCORAL);
                                        currentGc.fillRect(0, 0, 70, 70);
                                        currentGc.setFill(new ImagePattern(itemImage));
                                        currentGc.fillRect(0, 0, 70, 70);
                                    }
                                } else if (item instanceof Usable) {
                                    ((Usable) item).use(map);
                                    map.getCharacter().getItems().remove(item);
                                    root.getChildren().remove(currentCanva);

                                } else {
                                    map.getCharacter().getItems().remove(item);
                                    root.getChildren().remove(currentCanva);
                                }
                            });
                            root.getChildren().add(currentCanva);
                        }
                    }
                }
            } else IsInventoryOpen = false;
        });
        return button;
    }

    private void AddHoverDisplay(Equipment item, Canvas hoverInformations) {
        GraphicsContext hoverGC = hoverInformations.getGraphicsContext2D();
        hoverGC.setFill(Color.LIGHTGRAY);
        hoverGC.fillRect(0, 0, 100, 180);


        hoverGC.setFont(new Font("Arial", 30));

        int marginWidth = 10;
        int marginHeight = 20;

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/heart.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + item.getCaracteristics().getHp(), marginWidth + 50, marginHeight + 20);

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/attack.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight + 40, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + item.getCaracteristics().getAttack(), marginWidth + 50, marginHeight + 60);

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/armor.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight + 80, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + item.getCaracteristics().getArmor(), marginWidth + 50, marginHeight + 100);

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/money.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight + 120, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + item.getPrice(), marginWidth + 50, marginHeight + 140);
    }

    private List<Button> getNearInteractions(Group root, Map map, SceneManager sceneManager) {
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
                if (entity instanceof Chest) { //When interacting with a chest
                    addChestInteractionDisplay(root, map, sceneManager, (Chest) entity);
                } else if(entity instanceof Monster){
                    CombatManager combatManager = new CombatManager(map.getCharacter(), (Monster) entity, root, map);
                    //Display initialisation
                    combatManager.enterCombatLoop();
                }else {

                    root.getChildren().clear();
                    entity.interact(map, entity);
                    sceneManager.addAll();

                }
            });
            buttons.add(button);
        }
        return buttons;
    }

    private void addChestInteractionDisplay(Group root, Map map, SceneManager sceneManager, Chest entity) {
        Group chestRoot = new Group();

        // CHARACTER INVENTORY
        Canvas inventoryCanvas = new Canvas(550, 600);
        inventoryCanvas.setLayoutX(0);
        inventoryCanvas.setLayoutY(50);
        GraphicsContext gc = inventoryCanvas.getGraphicsContext2D();
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, 600, 600);
        root.getChildren().add(inventoryCanvas);

        var items = map.getCharacter().getItems();
        var itemsIterator = items.iterator();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                if (itemsIterator.hasNext()) {
                    var item = itemsIterator.next();
                    var itemImage = new Image(item.getImagePath(), 70, 70, true, false);

                    Canvas currentCanva = new Canvas(70, 70);
                    currentCanva.setLayoutX(0 + (j * 70) + (j * 40) + 30);
                    currentCanva.setLayoutY(50 + (i * 70) + (i * 40) + 30);

                    GraphicsContext currentGc = currentCanva.getGraphicsContext2D();
                    if (item instanceof Equipment) {
                        if (((Equipment) item).isEquiped()) {
                            currentGc.setFill(Color.RED);
                            currentGc.fillRect(0, 0, 70, 70);
                        }

                        /*
                        Canvas hoverInformations = new Canvas(100, 180);
                        hoverInformations.setLayoutX(currentCanva.getLayoutX() + currentCanva.getWidth() + 20);
                        hoverInformations.setLayoutY(currentCanva.getLayoutY() - 20);

                        AddHoverDisplay((Equipment) item, hoverInformations);

                        currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                            if (newValue) root.getChildren().add(hoverInformations);
                            else root.getChildren().remove(hoverInformations);
                        });
*/

                    }
                    currentGc.setFill(new ImagePattern(itemImage));
                    currentGc.fillRect(0, 0, 70, 70);

                    currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                        System.out.println("Clicked on item");
                        if (map.getCharacter().hasItem(item)) {
                            map.getCharacter().removeItem(item);
                            entity.addItemInChest(item);
                        }
                        root.getChildren().clear();
                        sceneManager.addAll();
                        addChestInteractionDisplay(root, map, sceneManager, entity);
                    });
                    root.getChildren().add(currentCanva);
                }
            }
        }


        // CHEST INVENTORY
        Canvas chestCanvas = new Canvas(550, 600);
        chestCanvas.setLayoutX(570);
        chestCanvas.setLayoutY(50);
        GraphicsContext chestGc = chestCanvas.getGraphicsContext2D();
        chestGc.setFill(Color.LIGHTCORAL);
        chestGc.fillRect(0, 0, 550, 600);
        root.getChildren().add(chestCanvas);
        var chestItems = entity.getItems();
        var chestItemsIterator = chestItems.iterator();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                if (chestItemsIterator.hasNext()) {
                    var item = chestItemsIterator.next();
                    String path = item.getImagePath();
                    var itemImage = new Image(path, 70, 70, true, false);

                    Canvas currentCanva = new Canvas(70, 70);
                    currentCanva.setLayoutX(550 + (j * 70) + (j * 40) + 30);
                    currentCanva.setLayoutY(50 + (i * 70) + (i * 40) + 30);

                    GraphicsContext currentGc = currentCanva.getGraphicsContext2D();
                    if (item instanceof Equipment) {
                        if (((Equipment) item).isEquiped()) {
                            currentGc.setFill(Color.RED);
                            currentGc.fillRect(0, 0, 70, 70);
                        }

                        /*
                        Canvas hoverInformations = new Canvas(100, 180);
                        hoverInformations.setLayoutX(currentCanva.getLayoutX() + currentCanva.getWidth() + 20);
                        hoverInformations.setLayoutY(currentCanva.getLayoutY() - 20);

                        AddHoverDisplay((Equipment) item, hoverInformations);

                        currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                            if (newValue) root.getChildren().add(hoverInformations);
                            else root.getChildren().remove(hoverInformations);
                        });

                         */
                    }
                    currentGc.setFill(new ImagePattern(itemImage));
                    currentGc.fillRect(0, 0, 70, 70);


                    currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                        System.out.println("Clicked on item");
                        if (chestItems.contains(item)) {
                            chestItems.remove(item);
                            map.getCharacter().addItem(item);
                        }
                        root.getChildren().clear();
                        sceneManager.addAll();
                        addChestInteractionDisplay(root, map, sceneManager, entity);
                    });


                    root.getChildren().add(currentCanva);
                }

            }
        }
    }
}


