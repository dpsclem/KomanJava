package GUI;

import java.util.concurrent.ThreadLocalRandom;

public class Map {
    private Caracter _caracter;
    private Cell[][] _table;

    public Map(Cell[][] table) {
        _table = table;
    }

    public Map(Cell[][] table, Caracter caracter) {
        _table = table;
        _caracter = caracter;
    }

    public static Map CreateRandomMap(){
        var table = new Cell[30][40];
        for(int i = 0; i < 30; i++){
            for(int j = 0; j < 40; j++){
                if (ThreadLocalRandom.current().nextInt(1, 11) == 1) table[i][j] = new Cell(CellMaterial.Wall, i, j);
                else if (ThreadLocalRandom.current().nextInt(1, 51) == 1) table[i][j] = new Cell(CellMaterial.Door, i, j);
                else table[i][j] = new Cell(CellMaterial.Floor, i, j);
            }
        }
        return new Map(table);
    }

    public void UpdateWithRandomMap(){
        _table = new Cell[30][40];
        for(int i = 0; i < 30; i++){
            for(int j = 0; j < 40; j++){
                if (ThreadLocalRandom.current().nextInt(1, 11) == 1) _table[i][j] = new Cell(CellMaterial.Wall, i, j);
                else if (ThreadLocalRandom.current().nextInt(1, 51) == 1) _table[i][j] = new Cell(CellMaterial.Door, i, j);
                else _table[i][j] = new Cell(CellMaterial.Floor, i, j);
            }
        }
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
        if (y == _caracter.getX() && x == _caracter.getY()) {
            return new Cell(_caracter.getColor(), _caracter.getX(), _caracter.getY());
        }
        return _table[y][x];
    }

    public int GetCaracterX() {
        return _caracter.getX();
    }

    public int GetCaracterY() {
        return _caracter.getY();
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
}
