package GUI;

import Entity.*;
import Item.*;
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
    private Cell[][] table;
    @Expose
    private Character character;

    @Expose
    private List<Entity> entities = new ArrayList<Entity>();

    @Expose
    private List<MapItem> Items = new ArrayList<MapItem>();

    private static int MapWidth = 25;
    private static int MapHeight = 15;
    public Map(Cell[][] table) {
        this.table = table;
    }

    public Map(Cell[][] table, Character character) {
        this.table = table;
        this.character = character;
    }
/*
    public static Map CreateRandomMap(){
        var table = new Cell[Map.MapWidth][Map.MapHeight];
        for(int i = 0; i < Map.MapWidth; i++){
            for(int j = 0; j < Map.MapHeight; j++){
                if (ThreadLocalRandom.current().nextInt(1, 11) == 1) table[i][j] = new Cell(CellMaterial.Wall, i, j);
                else table[i][j] = new Cell(CellMaterial.Floor, i, j);
            }
        }
        var randomMap = new Map(table);
        randomMap.addItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 3, 3);
        randomMap.addItemOnMap(new Item("pioche", 10, "file:resources/graphics/sprite/pioche.png"), 3, 5);
        randomMap.addItemOnMap(new Usable("moneybag", 0, false, UsableType.MONEYBAG, null, 15, "file:resources/graphics/sprite/moneybag.png"), 8,2);
        randomMap.addEntityOnMap(new Monster(2,2, EntityStatus.INACTIVE, EntityType.MONSTER, "file:resources/graphics/sprite/monster1.gif" ,new Caracteristics(5,10,7,7),null));
        randomMap.addEntityOnMap(new Trap(2,5, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/black_hole.png", TrapType.BLACKHOLE));
        randomMap.addEntityOnMap(new Trap(2,8, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/dart.png", TrapType.DART));

        return randomMap;
    }

    public void UpdateWithRandomMap(){
        table = new Cell[Map.MapWidth][Map.MapHeight];
        for(int i = 0; i < Map.MapWidth; i++){
            for(int j = 0; j < Map.MapHeight; j++){
                if (ThreadLocalRandom.current().nextInt(1, 11) == 1) table[i][j] = new Cell(CellMaterial.Wall, i, j);
                else table[i][j] = new Cell(CellMaterial.Floor, i, j);
            }
        }

        this.addItemOnMap(new Item("key", 10, "file:resources/graphics/sprite/key.png"), 3, 3);
        this.addItemOnMap(new Item("pioche", 10, "file:resources/graphics/sprite/pioche.png"), 3, 5);
        this.addItemOnMap(new Item("coin", 10, "file:resources/graphics/sprite/moneybag.png"), 8, 5);
        this.addEntityOnMap(new Monster(5,5, EntityStatus.INACTIVE, EntityType.MONSTER,"file:resources/graphics/sprite/monster1.gif" ,new Caracteristics(45,10,7,7),null));
        this.addEntityOnMap(new Trap(2,5, EntityStatus.INACTIVE, EntityType.TRAP,"file:resources/graphics/sprite/black_hole.png", TrapType.BLACKHOLE));
        this.addEntityOnMap(new Trap(2,8, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/dart.png", TrapType.DART));

    }

*/
    public int getTableWidth(){
        return table[0].length;
    }

    public int getTableHeight(){
        return table.length;
    }

    public Cell getCellFromCoordinate(int x, int y){
        return table[y][x];
    }

    public Cell getCharacterCell(){
        return new Cell(character.getImgPath(), character.getX(), character.getY());
    }
    
    public Character getCharacter() {
        return character;
    }

    public Coordinates getCharacterCoordinates(){
        return new Coordinates(character.getX(), character.getY());
    }

    public void updatePlayerPosition(Coordinates coord) {
        character.setX(coord.X);
        character.setY(coord.Y);
    }

    public void setCaracter(Character character) {
        this.character = character;
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
        int newX = character.getX() + x;
        int newY = character.getY() + y;
        System.out.println(newX + ":" + newY);
        if (newX < 0 || newX >= getTableHeight() || newY < 0 || newY >= getTableWidth())
        {
            System.out.println("Going out of table");
            return getCharacterCoordinates();
        }
        var futureCell = getCellFromCoordinate(newY, newX);
        System.out.println(futureCell.getMaterialProperties());

        if (entities.stream().filter(e -> e.getX() == newX && e.getY() == newY && e.getType() != EntityType.TRAP).count() > 0) { //Entity on future cell
            return getCharacterCoordinates();
        }
        if (futureCell.getMaterialProperties().IsWalkable  //Moving on floor
            && futureCell.getMaterialProperties().IsPassable) {
            var itemtoDelete = new ArrayList<MapItem>();
            for (MapItem mapItem: Items) {
                if (mapItem.getX() == newX && mapItem.getY() == newY){
                    itemtoDelete.add(mapItem);
                    character.addItem(mapItem.getItem());
                }
            }
            Items.removeAll(itemtoDelete);

            return new Coordinates(newX, newY);
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
/*
    public static Map createFromSave(String filePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Map map = gson.fromJson(reader, Map.class);
        return map;
    }
*/
    public List<Entity> getEntities(){
        return this.entities;
    }

    public List<Entity> getNearEntities(int characterX, int characterY) {
        var nearEntities = new ArrayList<Entity>();

        for (Entity entity: entities) {
            if ((entity.getX() - characterX == 0)  && (entity.getY() - characterY == -1)  ||
                (entity.getX() - characterX == 0)  && (entity.getY() - characterY == 1) ||
                (entity.getX() - characterX == 0)  &&  (entity.getY() - characterY == -1) ||
                (entity.getX() - characterX == 0)  &&  (entity.getY() - characterY == 1)  ||
                (entity.getY() - characterY == 0)  &&  (entity.getX() - characterX == -1) ||
                (entity.getY() - characterY == 0)  &&  (entity.getX() - characterX == 1) ||
                (entity.getY() - characterY == 0)  &&  (entity.getX() - characterX == -1)   ||
                (entity.getY() - characterY == 0)   &&  (entity.getX() - characterX == 1) ||
                (entity.getX() - characterX ==0) && (entity.getY() - characterY == 0))
            {
                nearEntities.add(entity);
            }
        }
        return nearEntities;
    }

    public static Map createFirstLevel(){
        //Creates walls and floors
        var table = new Cell[Map.MapWidth][Map.MapHeight];
        for(int i = 0; i < Map.MapWidth; i++){
            for(int j = 0; j < Map.MapHeight; j++){

                table[i][j] = new Cell(CellMaterial.Floor, i, j);
            }

        }
        //vertical walls creation
        for(int j = 0; j < Map.MapHeight; j++){

            table[0][j] = new Cell(CellMaterial.Wall, 0, j);
            table[24][j] = new Cell(CellMaterial.Wall, 24, j);
            table[10][j] = new Cell(CellMaterial.Wall, 10, j);
            table[16][j] = new Cell(CellMaterial.Wall, 16, j);
            table[17][j] = new Cell(CellMaterial.Wall, 17, j);
        }
        for(int j = 0; j < 10; j++){
            table[4][j] = new Cell(CellMaterial.Wall, 4, j);
        }
        for(int j = 9; j < 14; j++){
            table[6][j] = new Cell(CellMaterial.Wall, 6, j);
        }
        //Horizontal wall creation
        for(int j = 0; j < Map.MapWidth; j++){
            table[j][0] = new Cell(CellMaterial.Wall, j, 0);
            table[j][14] = new Cell(CellMaterial.Wall, j, 14);
        }
        for(int j = 0; j < 7; j++){
            table[j][9] = new Cell(CellMaterial.Wall, j, 9);
        }
        for(int j = 11; j <16; j++){
            table[j][9] = new Cell(CellMaterial.Wall, j, 9);
        }
        for(int j = 4; j <11; j++){
            table[j][3] = new Cell(CellMaterial.Wall, j, 3);
        }
        for(int j = 17; j <24; j++){
            table[j][1] = new Cell(CellMaterial.Wall, j, 1);
            table[j][13] = new Cell(CellMaterial.Wall, j, 13);
        }
        //Floors in place of walls to make a passage
        table[0][7] = new Cell(CellMaterial.Floor, 0, 7);
        table[24][7] = new Cell(CellMaterial.Floor, 24, 7);
        table[7][3] = new Cell(CellMaterial.Floor, 7, 3);
        table[6][11] = new Cell(CellMaterial.Floor, 6, 11);
        table[14][9] = new Cell(CellMaterial.Floor, 14, 9);
        table[16][5] = new Cell(CellMaterial.Floor, 16, 5);
        table[17][5] = new Cell(CellMaterial.Floor, 17, 5);


        var level = new Map(table);
        //Adds items and entities to the map
            //Adds monster to the map
        var monsterDL = new ArrayList<Item>();
        monsterDL.add( new Equipment("Sword", 60,  EquipmentType.SWORD,new Caracteristics(25,15,10,0), "file:resources/graphics/interface/attack.png"));
        level.addEntityOnMap(new Monster(12, 11, EntityStatus.INACTIVE, EntityType.MONSTER,
                "file:resources/graphics/sprite/bat_monster.gif", new Caracteristics(10, 10, 11, 11),monsterDL));
        level.addEntityOnMap(new Monster(23,7, EntityStatus.INACTIVE, EntityType.MONSTER,
                "file:resources/graphics/sprite/monster1.gif" ,new Caracteristics(12,14,25,25),null));
        level.addEntityOnMap(new Monster(6,11, EntityStatus.INACTIVE, EntityType.MONSTER,
                "file:resources/graphics/sprite/bat_monster.gif" ,new Caracteristics(5,10,7,7),null));
            //Adds money bag usable
        level.addItemOnMap(new Usable("moneybag", 15, false, UsableType.MONEYBAG, null, 15, "file:resources/graphics/sprite/moneybag.png"), 6, 1);
        level.addItemOnMap(new Usable("moneybag", 15, false, UsableType.MONEYBAG, null, 15, "file:resources/graphics/sprite/moneybag.png"), 8, 1);
        level.addItemOnMap(new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,20,15,0), "file:resources/graphics/sprite/equipements/chestplate1.png"), 13, 1);

            //Adds merchant NPC on the map
        var itemsInMerchant = new ArrayList<Item>();
        itemsInMerchant.add(new Equipment("Shield", 25,  EquipmentType.SHIELD,new Caracteristics(0,15,0,0), "file:resources/graphics/sprite/equipements/shield1.png"));
        itemsInMerchant.add(new Equipment("Sword", 10,  EquipmentType.SWORD,new Caracteristics(10,0,10,0), "file:resources/graphics/interface/attack.png"));
        itemsInMerchant.add(new Item("key", 50,"file:resources/graphics/sprite/key.png"));
        var merchant = new Merchant(3, 11, itemsInMerchant, EntityStatus.INACTIVE, EntityType.NPC_MERCHANT, "file:resources/graphics/sprite/merchant.png");
        level.addEntityOnMap(merchant);

        //Adds doors
        var door = new Entity(4,4, EntityStatus.CLOSE, EntityType.DOOR, "file:resources/graphics/sprite/door.png");
        level.addEntityOnMap(door);
        var door2 = new Entity(10,11, EntityStatus.CLOSE, EntityType.DOOR, "file:resources/graphics/sprite/door.png");
        level.addEntityOnMap(door2);

            //Adds a chest with items
        var itemsInChest = new ArrayList<Item>();
        itemsInChest.add(new Equipment("Sword", 10,  EquipmentType.SWORD,new Caracteristics(15,0,0,0), "file:resources/graphics/sprite/equipements/attack.png"));
        itemsInChest.add(new Equipment("Chestplate", 10,  EquipmentType.CHESTPLATE,new Caracteristics(0,10,10,0), "file:resources/graphics/sprite/equipements/chestplate1.png"));
        itemsInChest.add(new Item("key", 10, "file:resources/graphics/sprite/key.png"));
        var chest = new Chest(2,1, EntityStatus.INACTIVE, EntityType.CHEST,itemsInChest, "file:resources/graphics/sprite/chest.png");
        level.addEntityOnMap(chest);

            //Adds traps
        level.addEntityOnMap(new Trap(12,2, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/black_hole.png", TrapType.BLACKHOLE));
        level.addEntityOnMap(new Trap(14,2, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/black_hole.png", TrapType.BLACKHOLE));
        level.addEntityOnMap(new Trap(13,3, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/black_hole.png", TrapType.BLACKHOLE));
        level.addEntityOnMap(new Trap(7,3, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/dart.png", TrapType.DART));
        level.addEntityOnMap(new Trap(16,5, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/dart.png", TrapType.DART));
        level.addEntityOnMap(new Trap(17,5, EntityStatus.INACTIVE, EntityType.TRAP, "file:resources/graphics/sprite/dart.png", TrapType.DART));

        return level;
    }
}
