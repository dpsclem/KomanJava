package GUI;

import com.google.gson.annotations.Expose;
import javafx.scene.paint.Color;

import java.util.jar.Attributes;

public class CellMaterialProperties {
    @Expose
    public boolean IsWalkable;
    @Expose
    public boolean IsPassable;
    @Expose
    public String  ImgPath;

    private static String ImgPathDoor = "file:resources/graphics/sprite/door.png";
    private static String ImgPathWall = "file:resources/graphics/sprite/wall.png";
    private static String ImgPathFloor = "file:resources/graphics/sprite/floor.png";


    public CellMaterialProperties(boolean IsWalkable, boolean IsPassable, String imgPath) {
        this.IsWalkable = IsWalkable;
        this.IsPassable = IsPassable;
        this.ImgPath = imgPath;
    }

    public static CellMaterialProperties GetCellMaterialProperties(CellMaterial cellMaterial) {
        switch (cellMaterial) {
            case Wall:
                return new CellMaterialProperties(false, false, ImgPathWall);
            case Floor:
                return new CellMaterialProperties(true, true, ImgPathFloor);
            case Door:
                return new CellMaterialProperties(false, true, ImgPathDoor);
            default:
                return new CellMaterialProperties(true, true, ImgPathFloor);
        }
    }

    public String toString() {
        return "IsWalkable: " + IsWalkable + " | IsPassable: " + IsPassable + " | ImgPath: " + ImgPath;
    }
}
