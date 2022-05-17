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
    private Character _character;

    @Expose
    private List<Entity> entities = new ArrayList<Entity>();

    @Expose
    private List<MapItem> Items = new ArrayList<MapItem>();
    private static int MapWidth = 25;
    private static int MapHeight = 15;
    public Map(Cell[][] table) {
        _table = table;
    }

    public Map(Cell[][] table, Character character) {
        _table = table;
        _character = character;
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
        randomMap.addItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 3, 3);
        randomMap.addItemOnMap(new Item("pioche", 10, "file:resources/graphics/sprite/pioche.png"), 3, 5);
        randomMap.addEntityOnMap(new Entity(2,2,EntityStatus.INACTIVE,EntityType.MONSTER,"file:resources/graphics/sprite/monster1.gif" ));

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
        this.addItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 3, 3);
        this.addItemOnMap(new Item("pioche", 10, "file:resources/graphics/sprite/pioche.png"), 3, 5);
        this.addEntityOnMap(new Entity(5,5,EntityStatus.INACTIVE,EntityType.MONSTER,"file:resources/graphics/sprite/monster1.gif" ));
    }


    public int getTableWidth(){
        return _table[0].length;
    }

    public int getTableHeight(){
        return _table.length;
    }

    public Cell getCellFromCoordinate(int x, int y){
        return _table[y][x];
    }

    public Cell getCharacterCell(){
        return new Cell(_character.getImgPath(), _character.getX(), _character.getY());
    }
    
    public Character getCharacter() {
        return _character;
    }

    public Coordinates getCharacterCoordinates(){
        return new Coordinates(_character.getX(), _character.getY());
    }

    public void updatePlayerPosition(Coordinates coord) {
        _character.setX(coord.X);
        _character.setY(coord.Y);
    }

    public void setCaracter(Character character) {
        _character = character;
    }

    public void moveCaracterUp() {
        Coordinates futureCoord = getFutureCoordinatesForMovingInto(0,-1);
        updatePlayerPosition(futureCoord);
    }

    public void moveCaracterDown() {
        Coordinates futureCoord = getFutureCoordinatesForMovingInto(0,1);
        updatePlayerPosition(futureCoord);
    }

    public void moveCaracterLeft() {
        Coordinates futureCoord = getFutureCoordinatesForMovingInto(-1,0);
        updatePlayerPosition(futureCoord);
    }

    public void moveCaracterRight() {
        Coordinates futureCoord = getFutureCoordinatesForMovingInto(1,0);
        updatePlayerPosition(futureCoord);
    }

    private Coordinates getFutureCoordinatesForMovingInto(int x, int y) {
        int newX = _character.getX() + x;
        int newY = _character.getY() + y;
        System.out.println(newX + ":" + newY);
        if (newX < 0 || newX >= getTableHeight() || newY < 0 || newY >= getTableWidth())
        {
            System.out.println("Going out of table");
            return getCharacterCoordinates();
        }
        var futureCell = getCellFromCoordinate(newY, newX);
        System.out.println(futureCell.getMaterialProperties());

        if (futureCell.getMaterialProperties().IsWalkable  //Moving on floor
            && futureCell.getMaterialProperties().IsPassable) {
            var itemtoDelete = new ArrayList<MapItem>();
            for (MapItem mapItem: Items) {
                if (mapItem.getX() == newX && mapItem.getY() == newY){
                    itemtoDelete.add(mapItem);
                    _character.addItem(mapItem.getItem());
                }
            }
            Items.removeAll(itemtoDelete);
            return new Coordinates(newX, newY);
        }
        if (!futureCell.getMaterialProperties().IsWalkable //Moving through door
                && futureCell.getMaterialProperties().IsPassable
                && canMoveCaracterAt(new Coordinates(newX+x, newY+y))) {
            return new Coordinates(newX+x, newY+y); //Go through the door, so 1 more cell on the same direction
        }
        return getCharacterCoordinates();
    }

    private boolean canMoveCaracterAt(Coordinates newCoords){
        if (newCoords.X < 0 || newCoords.X >= getTableHeight() || newCoords.Y < 0 || newCoords.Y >= getTableWidth()) return false;

        var futureCell = getCellFromCoordinate(newCoords.Y, newCoords.X);
        if (futureCell.getMaterialProperties().IsWalkable
                && futureCell.getMaterialProperties().IsPassable) {
            return true;
        }
        return false;
    }

    public void addItemOnMap(Item item, int x, int y){
        Items.add(new MapItem(item, x, y));
    }

    public void addEntityOnMap(Entity entity){
        entities.add(entity);
    }

    public List<Rectangle> getItemRectangle(){
        var rectangles = new ArrayList<Rectangle>();

        for (MapItem mapItem: Items) {
            var rectangle = new Rectangle(mapItem.getX()*Cell.Width, mapItem.getY()*Cell.Height, Cell.Height, Cell.Width);
            Image img = new Image(mapItem.getItem().getImagePath(), Cell.Width, Cell.Height, true, false);
            rectangle.setFill(new ImagePattern(img));

            rectangles.add(rectangle);
        }

        return rectangles;
    }

    public String getSaveFormat() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();;
        //var mapDto = new MapDTO(_table, _caracter);
        return gson.toJson(this);
    }

    public static Map createFromSave(String filePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Map map = gson.fromJson(reader, Map.class);
        return map;
    }

    public List<Entity> getEntities(){
        return this.entities;
    }

}
