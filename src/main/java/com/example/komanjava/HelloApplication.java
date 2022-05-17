package com.example.komanjava;
import GUI.*;
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

        var sceneManager = new SceneManager(root, 1350, 850, Color.WHITE);
        Map randomMap = Map.CreateRandomMap();
        Caracter caracter = new Caracter(1,1);
        caracter.setCaracteristics(new Caracteristics(5,5,20));
        var shield = new Equipment("Shield", 10,  EquipmentType.SHIELD,new Caracteristics(0,20,0), "file:resources/graphics/sprite/equipements/shield1.png");
        caracter.addItem(shield);

        var chestplate = new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10), "file:resources/graphics/sprite/equipements/chestplate1.png");
        caracter.addItem(chestplate);

        var sword = new Equipment("Sword", 10,  EquipmentType.CHESTPLATE,new Caracteristics(15,0,0), "file:resources/graphics/sprite/equipements/attack.png");
        randomMap.AddItemOnMap(sword, 4,2);

        randomMap.SetCaracter(caracter);
        String jsonSave = randomMap.GetSaveFormat();
        PrintWriter writer = null;

        writer = new PrintWriter("saves/save1.json", "UTF-8");

        writer.println(jsonSave);
        writer.close();
        //Map randomMap = Map.CreateFromSave("saves/save1.json");

        sceneManager.Initialize(randomMap);


        System.out.println("Launching");
        stage.setScene(sceneManager.GetScene());
        stage.show();
    }





    public static void main(String[] args) {
        launch();
    }
}