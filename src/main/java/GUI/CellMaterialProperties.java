package GUI;

import com.google.gson.annotations.Expose;

public class CellMaterialProperties {
    @Expose
    public boolean IsWalkable;
    @Expose
    public boolean IsPassable;
    @Expose
    public boolean IsReplace;
    @Expose
    public String  ImgPath;

    private static String ImgPathWall = "file:resources/graphics/sprite/wall.png";
    private static String ImgPathFloor = "file:resources/graphics/sprite/floor.png";


    public CellMaterialProperties(boolean IsWalkable, boolean IsPassable, String imgPath) {
        this.IsWalkable = IsWalkable;
        this.IsPassable = IsPassable;
        this.ImgPath = imgPath;
    }

    public static CellMaterialProperties getCellMaterialProperties(CellMaterial cellMaterial) {
        switch (cellMaterial) {
            case Wall:
                return new CellMaterialProperties(false, false, ImgPathWall);
            case Floor:
                return new CellMaterialProperties(true, true, ImgPathFloor);
            default:
                return new CellMaterialProperties(true, true, ImgPathFloor);
        }
    }

    public String toString() {
        return "IsWalkable: " + IsWalkable + " | IsPassable: " + IsPassable + " | ImgPath: " + ImgPath;
    }
}
