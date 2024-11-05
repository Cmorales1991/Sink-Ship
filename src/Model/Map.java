package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Map {

    List<Coordinate> map = new ArrayList<>();

    public Map() {
        // CREATE EMPTY MAP, 10x10
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                map.add(new Coordinate(i, j, false));
            }
        }
    }

    public List<Coordinate> getMap() {
        return map;
    }

    // METHOD FOR TESTING
    public void printMap() {
        for(Coordinate c : map) {
            System.out.println(c.getX() + " " + c.getY() + " " + c.isShip());
        }
    }
}
