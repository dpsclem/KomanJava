package GUI;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
    }

    public Cell(String imgPath, int x, int y) {
        _x = x*Width;
        _y = y*Height;
        rectangle = new Rectangle(_x,_y, Height, Width);
        Image imgCharacter = new Image(imgPath, 48, 48, true, false);
        rectangle.setFill(new ImagePattern(imgCharacter));
    }

    public Rectangle GetRectangle() {
        if (rectangle != null) return rectangle; //Rectangle is already created for character

        if (_materialProperties == null) {
            _materialProperties = CellMaterialProperties.GetCellMaterialProperties(_material);
        }
        rectangle = new Rectangle(_x, _y, Height, Width);
        Image img = new Image(_materialProperties.ImgPath, Width, Height, true, false);
        rectangle.setFill(new ImagePattern(img));
        return rectangle;
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
