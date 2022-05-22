package SceneManager;

import CombatLogic.CombatManager;
import Entity.*;
import GUI.*;
import GUI.Character;
import Item.*;
import Item.Equipment;
import Item.EquipmentType;
import Item.Item;
import Item.Usable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountedCompleter;

import static java.awt.SystemColor.text;

public class SceneButtons {

    private boolean IsInventoryOpen = false;


    public List<Node> getButtons(Group root, Map map, SceneManager sceneManager) {
        var buttons = new ArrayList<Node>();
        buttons.add(getInventoryButton(root, map, sceneManager));
        buttons.addAll(getNearInteractions(root, map, sceneManager));
        return buttons;
    }

    public boolean isInventoryOpen() {
        return IsInventoryOpen;
    }

    private Button getInventoryButton(Group root, Map map, SceneManager sceneManager) {
        Button button = new Button();

        button.setLayoutX(1280);
        button.setLayoutY(100);
        button.setPrefSize(40, 50);

        Image img = new Image("file:resources/graphics/bag.png", 70, 70, true, false);
        ImageView view = new ImageView(img);
        button.setGraphic(view);

        button.setOnAction(event -> {
            System.out.println("Open inventory");

            if (!IsInventoryOpen && !map.getCharacter().getIsInteracting()) {
                IsInventoryOpen = true;
                map.getCharacter().setIsInteracting(true);
                addInventoryDisplay(root, map, sceneManager);
            } else IsInventoryOpen = false;
        });
        return button;
    }
    private void addInventoryDisplay(Group root, Map map, SceneManager sceneManager) {
        Canvas inventoryCanvas = new Canvas(830, 500);
        inventoryCanvas.setLayoutX(185);
        inventoryCanvas.setLayoutY(110);
        GraphicsContext gc = inventoryCanvas.getGraphicsContext2D();
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, 830, 500);
        root.getChildren().add(inventoryCanvas);

        var hoverInformationCanvas = new ArrayList<Canvas>();
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

                        Canvas hoverInformations = getHoverInformations((Equipment) item, currentCanva);
                        hoverInformationCanvas.add(hoverInformations);
                        currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                            if (newValue) hoverInformations.setVisible(true);
                            else hoverInformations.setVisible(false);
                        });

                    }

                    currentGc.setFill(new ImagePattern(itemImage));
                    currentGc.fillRect(0, 0, 70, 70);

                    currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                        System.out.println("Clicked on item");
                        if (item instanceof Equipment) {
                            if (((Equipment) item).isEquiped()) {
                                map.getCharacter().unequipItem((Equipment) item); //Return true si l'item a été équipé. Si c'est le cas, on place un fond rouge sur la case de l'item
                            }
                            else
                                map.getCharacter().equipItem((Equipment) item);
                        }
                        else if (item instanceof Usable) {
                            ((Usable) item).use(map);
                            map.getCharacter().getItems().remove(item);
                            if (((Usable) item).getUsableType() == UsableType.POTION) {
                                map.getCharacter().heal(((Usable) item).getUsablePower());
                                map.getCharacter().removeItem(item);
                                root.getChildren().remove(currentCanva);
                            }

                        }

                        root.getChildren().clear();
                        sceneManager.addAll();
                        addInventoryDisplay(root, map, sceneManager);
                    });
                    root.getChildren().add(currentCanva);
                }
            }
        }
        root.getChildren().addAll(hoverInformationCanvas);

        Canvas closeButton = getCloseButton(root,map, sceneManager,inventoryCanvas.getWidth() + inventoryCanvas.getLayoutX() - 30, inventoryCanvas.getLayoutY());

        root.getChildren().add(closeButton);
    }

    private Canvas getCloseButton(Group root,Map map, SceneManager sceneManager,double x, double y) {
        Canvas closeButton = new Canvas(30, 30);
        closeButton.setLayoutX(x);
        closeButton.setLayoutY(y);

        GraphicsContext closeBtnGc = closeButton.getGraphicsContext2D();
        closeBtnGc.setFill(new ImagePattern(new Image("file:resources/graphics/closeButton.png", 30,30, true, false)));
        closeBtnGc.fillRect(0, 0, 30, 30);
        closeButton.addEventFilter(MouseEvent.MOUSE_CLICKED, closeBtnEvent -> {
            root.getChildren().clear();
            sceneManager.addAll();
            IsInventoryOpen = false;
            map.getCharacter().setIsInteracting(false);

        });
        return closeButton;
    }

    private Canvas getHoverInformations(Equipment item, Canvas currentCanva) {
        Canvas hoverInformations = new Canvas(120, 180);
        hoverInformations.setLayoutX(currentCanva.getLayoutX() + currentCanva.getWidth() + 20);
        hoverInformations.setLayoutY(currentCanva.getLayoutY() - 20);

        //AddHoverDisplay((Equipment) item, hoverInformations);
        GraphicsContext hoverGC = hoverInformations.getGraphicsContext2D();
        hoverGC.setFill(Color.LIGHTGRAY);
        hoverGC.fillRect(0, 0, 120, 180);


        hoverGC.setFont(new Font("Arial", 30));

        int marginWidth = 10;
        int marginHeight = 20;

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/heart.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + ((Equipment) item).getCaracteristics().getMaxHp(), marginWidth + 50, marginHeight + 20);

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/attack.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight + 40, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + ((Equipment) item).getCaracteristics().getAttack(), marginWidth + 50, marginHeight + 60);

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/armor.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight + 80, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + ((Equipment) item).getCaracteristics().getArmor(), marginWidth + 50, marginHeight + 100);

        hoverGC.setFill(new ImagePattern(new Image("file:resources/graphics/interface/money.png", 20, 20, true, false)));
        hoverGC.fillRect(marginWidth, marginHeight + 120, 30, 30);

        hoverGC.setFill(Color.BLACK);
        hoverGC.fillText("" + ((Equipment) item).getPrice(), marginWidth + 50, marginHeight + 140);
        hoverInformations.setVisible(false);
        return hoverInformations;
    }

    private Canvas getCloseButton(Group root, SceneManager sceneManager,double x, double y) {
        Canvas closeButton = new Canvas(30, 30);
        closeButton.setLayoutX(x);
        closeButton.setLayoutY(y);

        GraphicsContext closeBtnGc = closeButton.getGraphicsContext2D();
        closeBtnGc.setFill(new ImagePattern(new Image("file:resources/graphics/closeButton.png", 30,30, true, false)));
        closeBtnGc.fillRect(0, 0, 30, 30);
        closeButton.addEventFilter(MouseEvent.MOUSE_CLICKED, closeBtnEvent -> {
            root.getChildren().clear();
            sceneManager.addAll();
            IsInventoryOpen = false;
        });
        return closeButton;
    }

    private void AddHoverDisplay(Equipment item, Canvas hoverInformations) {

    }

    private List<Button> getNearInteractions(Group root, Map map, SceneManager sceneManager) {
        List<Button> buttons = new ArrayList<>();
        var characterX = map.getCharacter().getX();
        var characterY = map.getCharacter().getY();

        var nearEntities = map.getNearEntities(characterX, characterY);

        for (var entity : nearEntities) {

            if (entity instanceof Trap && entity.getX() == characterX && entity.getY() == characterY){
                ((Trap) entity).triggerTrap(map.getCharacter());
                //If players interacts with a trap, takes damages and HP fall under 0 : Death of the player
                if(map.getCharacter().getCaracteristics().getCurrentHP() <=0){
                    //Animation of death
                    Animation deathScreen = new Animation(2,"file:resources/graphics/interface/you_died_screen.png",root,800,400,200,170);
                    deathScreen.playAnimation();
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(deathScreen.getAnimationLength()), ev ->{
                        //Then plays death and respawn logic
                        root.getChildren().remove(root);
                        //Reloads the MAP and player position (Respawn)
                        sceneManager.resetAndRespawn(root,map);
                        //Gives move key controls back to the player
                        map.getCharacter().setIsInteracting(false);
                    }));
                    timeline.play();
                }
                return buttons;
            }
            else if(entity instanceof Monster){
                map.getCharacter().setIsInteracting(true);//controls need to be frozen
                CombatManager combatManager = new CombatManager(map.getCharacter(), (Monster) entity, root, map,sceneManager);
                //Display initialisation
                combatManager.enterCombatLoop();
                return buttons;
            }

            else if(!(entity instanceof Trap)){
                Button button = new Button();

                button.setLayoutX(1220);
                button.setLayoutY(180 + (nearEntities.indexOf(entity) * 50) + (nearEntities.indexOf(entity) * 10));
                button.setPrefSize(140, 50);

                button.setText("Interact with " + entity.getType());

                button.setOnAction(event -> {
                    if (entity instanceof Chest) { //When interacting with a chest
                        map.getCharacter().setIsInteracting(true);//controls need to be frozen
                        addChestInteractionDisplay(root, map, sceneManager, (Chest) entity);
                    } else if (entity instanceof Merchant) {
                        map.getCharacter().setIsInteracting(true);//controls need to be frozen
                        addMerchantInteractionDisplay(root, map, sceneManager, (Merchant) entity);

                    } else {
                        root.getChildren().clear();
                        entity.interact(map, entity);
                        sceneManager.addAll();
                    }
                });
                buttons.add(button);
            }

            if(entity instanceof Merchant){ //Adds dialog when walking near merchant NPC
                var dialogArea = new TextArea();
                var text = ((Merchant) entity).getDialog();
                Text t = new Text(text);
                t.setFont(dialogArea.getFont());
                StackPane pane = new StackPane(t);
                pane.layout();
                double width = t.getLayoutBounds().getWidth();
                double padding = 20 ;
                dialogArea.setMaxWidth(width+padding);
                dialogArea.setText(text);
                dialogArea.setMaxHeight(30);
                dialogArea.setOpacity(0.68);
                dialogArea.positionCaret(-1);
                dialogArea.styleProperty().set("-fx-display-caret: false;");



                dialogArea.setWrapText(true);
                dialogArea.setLayoutX((entity.getX() + 1) * Cell.Width);
                dialogArea.setLayoutY(entity.getY() * Cell.Height);
                root.getChildren().add(dialogArea);
            }

        }
        return buttons;
    }


    private void addMerchantInteractionDisplay(Group root, Map map, SceneManager sceneManager, Merchant entity) {
        var hoverInformationCanvas = new ArrayList<Canvas>();


        // CHARACTER INVENTORY
        Canvas inventoryCanvas = new Canvas(550, 600);
        inventoryCanvas.setLayoutX(0);
        inventoryCanvas.setLayoutY(50);
        GraphicsContext gc = inventoryCanvas.getGraphicsContext2D();
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, 600, 600);
        root.getChildren().add(inventoryCanvas);

        var pricesNodes = new ArrayList<Node>(); //We keep them and add them to the root later to make sure they are on top of the other nodes

        var items = map.getCharacter().getItems();
        var itemsIterator = items.iterator();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                if (itemsIterator.hasNext()) {
                    var item = itemsIterator.next();
                    var itemImage = new Image(item.getImagePath(), 70, 70, true, false);

                    Canvas currentCanva = new Canvas(70, 70);
                    currentCanva.setLayoutX(0 + (j * 70) + (j * 50) + 50);
                    currentCanva.setLayoutY(50 + (i * 70) + (i * 50) + 30);

                    GraphicsContext currentGc = currentCanva.getGraphicsContext2D();
                    if (item instanceof Equipment) {
                        if (((Equipment) item).isEquiped()) {
                            currentGc.setFill(Color.RED);
                            currentGc.fillRect(0, 0, 70, 70);
                        }
                        Canvas hoverInformations = getHoverInformations((Equipment)item, currentCanva);
                        hoverInformationCanvas.add(hoverInformations);
                        currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                            if (newValue) hoverInformations.setVisible(true);
                            else hoverInformations.setVisible(false);
                        });
                    }
                    currentGc.setFill(new ImagePattern(itemImage));
                    currentGc.fillRect(0, 0, 70, 70);




                    Canvas itemPriceCanva = new Canvas(70,20);
                    itemPriceCanva.setLayoutX(0 + (j * 70) + (j * 50) + 50);
                    itemPriceCanva.setLayoutY(50 + (i * 70) + (i * 50) + 100);

                    GraphicsContext itemPriceGc = itemPriceCanva.getGraphicsContext2D();

                    itemPriceGc.setFill(new ImagePattern(new Image("file:resources/graphics/interface/money.png", 50, 50, true, false)));
                    itemPriceGc.fillRect(5, 0, 20, 20);

                    itemPriceGc.setFont(new Font("Arial", 20) );
                    itemPriceGc.setFill(Color.WHITE);
                    itemPriceGc.fillText("" + item.getPrice(), 25,17);

                    pricesNodes.add(itemPriceCanva);

                    currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                        System.out.println("Clicked on item");
                        map.getCharacter().sellItem(item);
                        root.getChildren().clear();
                        sceneManager.addAll();
                        addMerchantInteractionDisplay(root, map, sceneManager, entity);
                    });

                    root.getChildren().add(currentCanva);

                }
            }
        }


        // MERCHANT INVENTORY
        Canvas merchantCanvas = new Canvas(550, 600);
        merchantCanvas.setLayoutX(570);
        merchantCanvas.setLayoutY(50);
        GraphicsContext merchantGc = merchantCanvas.getGraphicsContext2D();
        merchantGc.setFill(Color.LIGHTCORAL);
        merchantGc.fillRect(0, 0, 600, 600);
        root.getChildren().add(merchantCanvas);
        var merchantItems = entity.getItems();
        var merchantItemsIterator = merchantItems.iterator();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                if (merchantItemsIterator.hasNext()) {
                    var item = merchantItemsIterator.next();
                    String path = item.getImagePath();
                    var itemImage = new Image(path, 70, 70, true, false);

                    Canvas currentCanva = new Canvas(70, 70);
                    currentCanva.setLayoutX(550 + (j * 70) + (j * 50) + 50);
                    currentCanva.setLayoutY(50 + (i * 70) + (i * 50) + 30);

                    GraphicsContext currentGc = currentCanva.getGraphicsContext2D();
                    if (item instanceof Equipment) {
                        if (((Equipment) item).isEquiped()) {
                            currentGc.setFill(Color.RED);
                            currentGc.fillRect(0, 0, 70, 70);
                        }
                        Canvas hoverInformations = getHoverInformations((Equipment)item, currentCanva);
                        hoverInformationCanvas.add(hoverInformations);
                        currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                            if (newValue) hoverInformations.setVisible(true);
                            else hoverInformations.setVisible(false);
                        });
                    }
                    currentGc.setFill(new ImagePattern(itemImage));
                    currentGc.fillRect(0, 0, 70, 70);




                    Canvas itemPriceCanva = new Canvas(70,20);
                    itemPriceCanva.setLayoutX(550 + (j * 70) + (j * 50) + 50);
                    itemPriceCanva.setLayoutY(50 + (i * 70) + (i * 50) + 100);

                    GraphicsContext itemPriceGc = itemPriceCanva.getGraphicsContext2D();

                    itemPriceGc.setFill(new ImagePattern(new Image("file:resources/graphics/interface/money.png", 50, 50, true, false)));
                    itemPriceGc.fillRect(5, 0, 20, 20);

                    itemPriceGc.setFont(new Font("Arial", 20) );
                    itemPriceGc.setFill(Color.WHITE);
                    itemPriceGc.fillText("" + item.getPrice(), 25,17);

                    pricesNodes.add(itemPriceCanva);


                    currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                        System.out.println("Clicked on item");
                        entity.buyItem(item, map.getCharacter());
                        root.getChildren().clear();
                        sceneManager.addAll();
                        addMerchantInteractionDisplay(root, map, sceneManager, entity);
                    });


                    root.getChildren().add(currentCanva);
                }

            }
        }
        double x = merchantCanvas.getLayoutX()+merchantCanvas.getWidth()-30;
        double y = merchantCanvas.getLayoutY();
        Canvas closeButton = getCloseButton(root,map, sceneManager, x,y);
        root.getChildren().add(closeButton);

        root.getChildren().addAll(pricesNodes);
        root.getChildren().addAll(hoverInformationCanvas);


    }

    private void addChestInteractionDisplay(Group root, Map map, SceneManager sceneManager, Chest entity) {
        var hoverInformationCanvas = new ArrayList<Canvas>();

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
                        Canvas hoverInformations = getHoverInformations((Equipment)item, currentCanva);
                        hoverInformationCanvas.add(hoverInformations);
                        currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                            if (newValue) hoverInformations.setVisible(true);
                            else hoverInformations.setVisible(false);
                        });
                    }
                    currentGc.setFill(new ImagePattern(itemImage));
                    currentGc.fillRect(0, 0, 70, 70);

                    currentCanva.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                        System.out.println("Clicked on item");
                        if (map.getCharacter().hasItem(item)) {
                            if (item instanceof Equipment) {
                                if (((Equipment) item).isEquiped()) map.getCharacter().unequipItem((Equipment) item);
                            }
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
                        Canvas hoverInformations = getHoverInformations((Equipment)item, currentCanva);
                        hoverInformationCanvas.add(hoverInformations);
                        currentCanva.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                            if (newValue) hoverInformations.setVisible(true);
                            else hoverInformations.setVisible(false);
                        });
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
        root.getChildren().addAll(hoverInformationCanvas);
        double x = chestCanvas.getLayoutX()+chestCanvas.getWidth()-30;
        double y = chestCanvas.getLayoutY();
        Canvas closeButton = getCloseButton(root,map, sceneManager, x,y);
        root.getChildren().add(closeButton);

    }


}


