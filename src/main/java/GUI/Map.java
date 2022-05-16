package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Map {
    @Expose
    private Cell[][] _table;
    @Expose
    private Caracter _caracter;

    @Expose
    private List<Entity> entities = new ArrayList<Entity>();

    public List<MapItem> getItems() {
        return Items;
    }

    @Expose
    private List<MapItem> Items = new ArrayList<MapItem>();
    private static int MapWidth = 25;
    private static int MapHeight = 15;
    public Map(Cell[][] table) {
        _table = table;
    }

    public Map(Cell[][] table, Caracter caracter) {
        _table = table;
        _caracter = caracter;
    }

    public static Map CreateRandomMap(){
        var table = new Cell[Map.MapWidth][Map.MapHeight];
        for(int i = 0; i < Map.MapWidth; i++){
            for(int j = 0; j < Map.MapHeight; j++){
                if (ThreadLocalRandom.current().nextInt(1, 11) == 1) table[i][j] = new Cell(CellMaterial.Wall, i, j);
                else if (ThreadLocalRandom.current().nextInt(1, 51) == 1) table[i][j] = new Cell(CellMaterial.Door, i, j);
                else table[i][j] = new Cell(CellMaterial.Floor, i, j);
            }
        }
        var randomMap = new Map(table);
        randomMap.AddItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 3, 3);
        randomMap.AddItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 5, 3);
        randomMap.AddItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 4, 3);
        randomMap.AddItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 3, 6);
        randomMap.AddEntityOnMap(new Entity(2,2,EntityStatus.INACTIVE,EntityType.MONSTER,"file:resources/graphics/sprite/monster1.gif" ));

        return randomMap;
    }

    public void UpdateWithRandomMap(){
        _table = new Cell[Map.MapWidth][Map.MapHeight];
        for(int i = 0; i < Map.MapWidth; i++){
            for(int j = 0; j < Map.MapHeight; j++){
                if (ThreadLocalRandom.current().nextInt(1, 11) == 1) _table[i][j] = new Cell(CellMaterial.Wall, i, j);
                else if (ThreadLocalRandom.current().nextInt(1, 51) == 1) _table[i][j] = new Cell(CellMaterial.Door, i, j);
                else _table[i][j] = new Cell(CellMaterial.Floor, i, j);
            }
        }
        this.AddEntityOnMap(new Entity(5,5,EntityStatus.INACTIVE,EntityType.MONSTER,"file:resources/graphics/sprite/monster1.gif" ));
        this.AddItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 3, 3);
    }

    public int GetTableSize(){
        return _table.length * _table[0].length;
    }

    public int GetTableWidth(){
        return _table[0].length;
    }

    public int GetTableHeight(){
        return _table.length;
    }

    public Cell GetCellFromCoordinate(int x, int y){
        return _table[y][x];
    }

    public Cell GetCaracterCell(){
        return new Cell(_caracter.getImgPath(), _caracter.getX(), _caracter.getY());
    }
    
    public Caracter getCaracter() {
        return _caracter;
    }

    public Coordinates GetCaracterCoordinates(){
        return new Coordinates(_caracter.getX(), _caracter.getY());
    }

    public void UpdatePlayerPosition(Coordinates coord) {
        _caracter.setX(coord.X);
        _caracter.setY(coord.Y);
    }

    public void SetCaracter(Caracter caracter) {
        _caracter = caracter;
    }

    public void MoveCaracterUp() {
        Coordinates futureCoord = GetFutureCoordinatesForMovingInto(0,-1);
        UpdatePlayerPosition(futureCoord);
    }

    public void MoveCaracterDown() {
        Coordinates futureCoord = GetFutureCoordinatesForMovingInto(0,1);
        UpdatePlayerPosition(futureCoord);
    }

    public void MoveCaracterLeft() {
        Coordinates futureCoord = GetFutureCoordinatesForMovingInto(-1,0);
        UpdatePlayerPosition(futureCoord);
    }

    public void MoveCaracterRight() {
        Coordinates futureCoord = GetFutureCoordinatesForMovingInto(1,0);
        UpdatePlayerPosition(futureCoord);
    }

    private Coordinates GetFutureCoordinatesForMovingInto(int x, int y) {
        int newX = _caracter.getX() + x;
        int newY = _caracter.getY() + y;
        System.out.println(newX + ":" + newY);
        if (newX < 0 || newX >= GetTableHeight() || newY < 0 || newY >= GetTableWidth())
        {
            System.out.println("Going out of table");
            return GetCaracterCoordinates();
        }
        var futureCell = GetCellFromCoordinate(newY, newX);
        System.out.println(futureCell.getMaterialProperties());

        if (futureCell.getMaterialProperties().IsWalkable  //Moving on floor
            && futureCell.getMaterialProperties().IsPassable) {
            var itemtoDelete = new ArrayList<MapItem>();
            for (MapItem mapItem: Items) {
                if (mapItem.getX() == newX && mapItem.getY() == newY){
                    itemtoDelete.add(mapItem);
                    _caracter.addItem(mapItem.getItem());
                }
            }
            Items.removeAll(itemtoDelete);
            return new Coordinates(newX, newY);
        }
        if (!futureCell.getMaterialProperties().IsWalkable //Moving through door
                && futureCell.getMaterialProperties().IsPassable
                && CanMoveCaracterAt(new Coordinates(newX+x, newY+y))) {
            return new Coordinates(newX+x, newY+y); //Go through the door, so 1 more cell on the same direction
        }
        return GetCaracterCoordinates();
    }

    private boolean CanMoveCaracterAt(Coordinates newCoords){
        if (newCoords.X < 0 || newCoords.X >= GetTableHeight() || newCoords.Y < 0 || newCoords.Y >= GetTableWidth()) return false;

        var futureCell = GetCellFromCoordinate(newCoords.Y, newCoords.X);
        if (futureCell.getMaterialProperties().IsWalkable
                && futureCell.getMaterialProperties().IsPassable) {
            return true;
        }
        return false;
    }

    public void AddItemOnMap(Item item,int x,int y){
        Items.add(new MapItem(item, x, y));
    }



    public void AddEntityOnMap(Entity entity){
        entities.add(entity);
    }

    public List<Rectangle> GetItemRectangle(){
        var rectangles = new ArrayList<Rectangle>();

        for (MapItem mapItem: Items) {
            var rectangle = new Rectangle(mapItem.getX()*Cell.Width, mapItem.getY()*Cell.Height, Cell.Height, Cell.Width);
            Image img = new Image(mapItem.getItem().getImagePath(), Cell.Width, Cell.Height, true, false);
            rectangle.setFill(new ImagePattern(img));

            rectangles.add(rectangle);
        }

        return rectangles;
    }

    public String GetSaveFormat() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();;
        //var mapDto = new MapDTO(_table, _caracter);
        return gson.toJson(this);
    }

    public static Map CreateFromSave(String filePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Map map = gson.fromJson(reader, Map.class);
        return map;
    }

    public List<Entity> getEntities(){
        return this.entities;
    }

}
