package GUI;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
    public static int Width = 20;
    public static int Height = 20;
    private int _x;
    private int _y;

    public Rectangle rectangle;
    private CellMaterial _material;
    private CellMaterialProperties _materialProperties;

    public Cell(CellMaterial material, int x, int y) {
        _material = material;
        _materialProperties = CellMaterialProperties.GetCellMaterialProperties(material);
        _x = x*Width;
        _y = y*Height;
        rectangle = new Rectangle(_x, _y, Height, Width);
        rectangle.setFill(_materialProperties.Color);
    }

    public Cell(Color color, int x, int y) {
        _x = x*Width;
        _y = y*Height;
        rectangle = new Rectangle(_x,_y, Height, Width);
        rectangle.setFill(color);
    }

    public CellMaterial getMaterial() {
        return _material;
    }

    public CellMaterialProperties getMaterialProperties() {
        return _materialProperties;
    }

}
