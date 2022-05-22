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

        //Creates a new scene for runtime
        var sceneManager = new SceneManager(root, 1450, 850, Color.WHITE);

        //Creates the map
        Map map = Map.createFirstLevel();
        sceneManager.initialize(map);

        stage.setScene(sceneManager.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}