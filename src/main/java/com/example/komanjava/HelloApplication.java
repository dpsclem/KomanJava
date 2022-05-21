package com.example.komanjava;
import Entity.*;
import GUI.*;
import GUI.Character;
import Item.Equipment;
import Item.EquipmentType;
import Item.*;
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
        //Create main display Group
        Group root = new Group();
        stage.setTitle("Koman Java");

        var sceneManager = new SceneManager(root, 1450, 850, Color.WHITE);
        Map randomMap = Map.createFirstLevel();
        Character character = new Character(0,7);
        character.setCaracteristics(new Caracteristics(5,5,20,20));

        character.addMoney(50);

        var potion = new Usable("HealPotion", 25, false, UsableType.POTION, null, 15, "file:resources/graphics/sprite/healPotion.png");
        character.addItem(potion);

        var shield = new Equipment("Shield", 8,  EquipmentType.SHIELD,new Caracteristics(0,20,0,0), "file:resources/graphics/sprite/equipements/shield1.png");
        character.addItem(shield);

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