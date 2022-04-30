package GUI;

import com.google.gson.annotations.Expose;
import javafx.scene.paint.Color;

public class CellMaterialProperties {
    @Expose
    public boolean IsWalkable;
    @Expose
    public boolean IsPassable;
    @Expose
    public static Color Color;

    public CellMaterialProperties(boolean IsWalkable, boolean IsPassable, Color Color) {
        this.IsWalkable = IsWalkable;
        this.IsPassable = IsPassable;
        this.Color = Color;
    }

    public static CellMaterialProperties GetCellMaterialProperties(CellMaterial cellMaterial) {
        switch (cellMaterial) {
            case Wall:
                return new CellMaterialProperties(false, false, Color.BLACK);
            case Floor:
                return new CellMaterialProperties(true, true, Color.GRAY);
            case Door:
                return new CellMaterialProperties(false, true, Color.SANDYBROWN);
            case Caracter:
                return new CellMaterialProperties(false, false, Color.CYAN);
            default:
                return new CellMaterialProperties(true, true, Color.PINK);
        }
    }

    public String toString() {
        return "IsWalkable: " + IsWalkable + " | IsPassable: " + IsPassable + " | Color: " + Color;
    }
}
