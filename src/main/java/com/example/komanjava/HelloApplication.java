package com.example.komanjava;
import GUI.Caracter;
import GUI.Cell;
import GUI.Map;
import GUI.SceneManager;
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

        var sceneManager = new SceneManager(root, 1350, 850, Color.WHITE, "Koman Java");
        Map randomMap = Map.CreateRandomMap();
        Caracter caracter = new Caracter(1,1);
        randomMap.SetCaracter(caracter);

        String jsonSave = randomMap.GetSaveFormat();
        PrintWriter writer = new PrintWriter("saves/save1.json", "UTF-8");
        writer.println(jsonSave);
        writer.close();

        //Map randomMap = Map.CreateFromSave("saves/save1.json");

        List<Button> buttons = sceneManager.GetButtons();
        root.getChildren().addAll(buttons);
        FillSceneWithMap(root, randomMap);

        resetMapBtn.setOnAction(event -> {
            root.getChildren().clear();
            randomMap.UpdateWithRandomMap();
            var resetCaracter = new Caracter(1,1);
            randomMap.SetCaracter(resetCaracter);
            FillSceneWithMap(root, randomMap);
            root.getChildren().add(resetMapBtn);
        });


        scene.addEventFilter(KeyEvent.KEY_PRESSED, e->{
            root.getChildren().clear();
            root.getChildren().add(resetMapBtn);
            switch(e.getCode()) {
                case LEFT:
                    randomMap.MoveCaracterLeft();
                    FillSceneWithMap(root, randomMap);
                    System.out.println("Go left");
                    break;
                case RIGHT:
                    randomMap.MoveCaracterRight();
                    FillSceneWithMap(root, randomMap);
                    System.out.println("Go right");
                    break;
                case UP:
                    randomMap.MoveCaracterUp();
                    FillSceneWithMap(root, randomMap);
                    System.out.println("Go up");
                    break;
                case DOWN:
                    randomMap.MoveCaracterDown();
                    FillSceneWithMap(root, randomMap);
                    System.out.println("Go down");
                    break;
                default:
                    FillSceneWithMap(root, randomMap);
                    System.out.println("Other click detected");
                    break;
            }
        });
        System.out.println("Launching");
        stage.setScene(sceneManager.GetScene());
        stage.show();
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
    }

    public static void main(String[] args) {
        launch();
    }
}