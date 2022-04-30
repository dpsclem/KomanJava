package GUI;

import com.google.gson.annotations.Expose;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import com.google.gson.Gson;

public class Cell {
    @Expose
    private static int Width = 48;
    @Expose
    private static int Height = 48;
    @Expose
    private int _x;
    @Expose
    private int _y;

    private Rectangle rectangle;

    @Expose
    private CellMaterial _material;
    @Expose
    private CellMaterialProperties _materialProperties;

    public Cell(CellMaterial material, int x, int y) {
        _material = material;
        _materialProperties = CellMaterialProperties.GetCellMaterialProperties(material);
        _x = x*Width;
        _y = y*Height;
        rectangle = new Rectangle(_x, _y, Height, Width);
        rectangle.setFill(_materialProperties.Color);
    }

    public Rectangle GetRectangle() {
        if (rectangle == null) {
            rectangle = new Rectangle(_x, _y, Height, Width);
            _materialProperties = CellMaterialProperties.GetCellMaterialProperties(_material);
            rectangle.setFill(_materialProperties.Color);
        }
        return rectangle;
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

    public static int getWidth() {
        return Width;
    }
    public static int getHeight() {
        return Height;
    }

    public int getX() {
        return _x;
    }
    public int getY() {
        return _y;
    }
}
