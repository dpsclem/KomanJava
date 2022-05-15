package com.example.komanjava;
import GUI.*;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Group root = new Group();
        stage.setTitle("Koman Java");

        var sceneManager = new SceneManager(root, 1350, 850, Color.WHITE);
        Map randomMap = Map.CreateRandomMap();
        Caracter caracter = new Caracter(1,1);
        randomMap.SetCaracter(caracter);

        String jsonSave = randomMap.GetSaveFormat();
        PrintWriter writer = new PrintWriter("saves/save1.json", "UTF-8");
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