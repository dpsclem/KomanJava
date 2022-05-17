package com.example.komanjava;
import GUI.*;
import GUI.Character;
import SceneManager.SceneManager;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.PrintWriter;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Group root = new Group();
        stage.setTitle("Koman Java");

        var sceneManager = new SceneManager(root, 1450, 850, Color.WHITE);
        Map randomMap = Map.CreateRandomMap();
        Character character = new Character(1,1);
        character.setCaracteristics(new Caracteristics(5,5,20));
        var shield = new Equipment("Shield", 10,  EquipmentType.SHIELD,new Caracteristics(0,20,0), "file:resources/graphics/sprite/equipements/shield1.png");
        character.addItem(shield);

        var chestplate = new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10), "file:resources/graphics/sprite/equipements/chestplate1.png");
        character.addItem(chestplate);

        var sword = new Equipment("Sword", 10,  EquipmentType.CHESTPLATE,new Caracteristics(15,0,0), "file:resources/graphics/sprite/equipements/attack.png");
        randomMap.addItemOnMap(sword, 4,2);

        var chest = new Entity(0,0,EntityStatus.INACTIVE, EntityType.CHEST, "file:resources/graphics/sprite/chest.png");
        randomMap.addEntityOnMap(chest);

        var door = new Entity(5,1,EntityStatus.CLOSE, EntityType.DOOR, "file:resources/graphics/sprite/door.png");
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