package com.example.komanjava;
import Entity.*;
import GUI.*;
import GUI.Character;
import Item.Equipment;
import Item.EquipmentType;
import Item.Item;
import SceneManager.SceneManager;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.PrintWriter;


import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Group root = new Group();
        stage.setTitle("Koman Java");

        var sceneManager = new SceneManager(root, 1450, 850, Color.WHITE);
        Map randomMap = Map.CreateRandomMap();
        Character character = new Character(1,1);
        character.setCaracteristics(new Caracteristics(5,5,20,20));

        character.addMoney(50);

        var shield = new Equipment("Shield", 8,  EquipmentType.SHIELD,new Caracteristics(0,20,0,0), "file:resources/graphics/sprite/equipements/shield1.png");
        character.addItem(shield);

        var chestplate = new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png");
        character.addItem(chestplate);

        var sword = new Equipment("Sword", 10,  EquipmentType.CHESTPLATE,new Caracteristics(15,0,0,0), "file:resources/graphics/sprite/equipements/attack.png");
        randomMap.addItemOnMap(sword, 4,2);

        var itemsInChest = new ArrayList<Item>();
        itemsInChest.add(new Equipment("Sword", 10,  EquipmentType.SWORD,new Caracteristics(15,0,0,0), "file:resources/graphics/sprite/equipements/attack.png"));
        itemsInChest.add(new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png"));
        var chest = new Chest(0,0, EntityStatus.INACTIVE, EntityType.CHEST,itemsInChest, "file:resources/graphics/sprite/chest.png", TrapType.NONE);
        randomMap.addEntityOnMap(chest);

        var itemsInMerchant = new ArrayList<Item>();
        itemsInMerchant.add(new Equipment("Shield", 25,  EquipmentType.CHESTPLATE,new Caracteristics(0,15,0,0), "file:resources/graphics/sprite/equipements/shield1.png"));
        itemsInMerchant.add(new Equipment("Chestplate", 180,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png"));
        itemsInMerchant.add(new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png"));
        itemsInMerchant.add(new Equipment("Sword", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/interface/attack.png"));
        itemsInMerchant.add(new Equipment("Chestplate", 20,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png"));
        itemsInMerchant.add(new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png"));
        itemsInMerchant.add(new Equipment("Sword", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/interface/attack.png"));
        itemsInMerchant.add(new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png"));
        itemsInMerchant.add(new Equipment("Shield", 15,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/shield1.png"));
        var merchant = new Merchant(5, 0, itemsInMerchant, EntityStatus.INACTIVE, EntityType.NPC_MERCHANT, "file:resources/graphics/sprite/merchant.png", TrapType.NONE);
        randomMap.addEntityOnMap(merchant);

        var door = new Entity(5,1, EntityStatus.CLOSE, EntityType.DOOR, TrapType.NONE, "file:resources/graphics/sprite/door.png");
        randomMap.addEntityOnMap(door);

        randomMap.setCaracter(character);
        String jsonSave = randomMap.getSaveFormat();
        PrintWriter writer = null;

        writer = new PrintWriter("saves/save1.json", "UTF-8");

        writer.println(jsonSave);
        writer.close();
        //Map randomMap = Map.CreateFromSave("saves/save1.json");

        sceneManager.initialize(randomMap);


        System.out.println("Launching");
        stage.setScene(sceneManager.getScene());
        stage.show();
    }





    public static void main(String[] args) {
        launch();
    }
}