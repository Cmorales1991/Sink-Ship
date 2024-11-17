package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserMap {

    private List<Coordinate> map = new ArrayList<>();
    private List<Ship> ships = new ArrayList<>(); // Lista över alla skepp
    private final int MAP_SIZE = 10;
    private final int[] SHIPS = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    public UserMap() {
        // Create empty map, 10x10
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                map.add(new Coordinate(i, j, false));
            }
        }
        placeShips();
    }

    private void placeShips() {
        Random rand = new Random();
        for (int size : SHIPS) {
            boolean placed = false;
            while (!placed) {
                boolean horizontal = rand.nextBoolean();
                int startX = rand.nextInt(MAP_SIZE);
                int startY = rand.nextInt(MAP_SIZE);

                if (canPlaceShip(startX, startY, size, horizontal)) {
                    Ship newShip = new Ship("Ship-" + size, size, horizontal);
                    placeShip(startX, startY, size, horizontal, newShip);
                    ships.add(newShip);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(int startX, int startY, int size, boolean horizontal) {
        int endX = horizontal ? startX + size - 1 : startX;
        int endY = horizontal ? startY : startY + size - 1;

        // MAKE SURE SHIP FITS ON MAP
        if (endX >= MAP_SIZE || endY >= MAP_SIZE) return false;

        // CHECK SHIP'S SURROUNDING COORDINATES
        for (int x = Math.max(0, startX - 1); x <= Math.min(MAP_SIZE - 1, endX + 1); x++) {
            for (int y = Math.max(0, startY - 1); y <= Math.min(MAP_SIZE - 1, endY + 1); y++) {
                Coordinate coord = getCoordinate(x, y);
                if (coord.isShip()) {
                    return false;
                }
            }
        }
        return true; // IF PLACEMENT IS ALLOWED
    }

    private void placeShip(int startX, int startY, int size, boolean horizontal, Ship ship) {
        for (int i = 0; i < size; i++) {
            int x = horizontal ? startX + i : startX;
            int y = horizontal ? startY : startY + i;
            Coordinate coord = getCoordinate(x, y);
            coord.setShip(true);
            coord.setShipReference(ship);
            ship.getPositions().add(new int[]{x, y});
        }
    }

    private Coordinate getCoordinate(int x, int y) {
        return map.stream()
                .filter(coord -> coord.getX() == x && coord.getY() == y)
                .findFirst()
                .orElse(null);
    }

    public void takeAttack(int x, int y) {
        Coordinate c = getCoordinate(x, y);
        if (c.isShip()) {
            c.destroyShip();
        }
    }

    public boolean wasHit(int x, int y) {
        Coordinate c = getCoordinate(x, y);
        return c != null && c.isShip();
    }

    public boolean isShipSunk(int x, int y) {
        Coordinate coord = getCoordinate(x, y);
        if (coord == null || coord.getShipReference() == null) {
            return false; // Om ingen skeppsreferens hittas, är det inte en del av ett skepp
        }

        Ship ship = coord.getShipReference();

        // Kontrollera om alla delar av skeppet är förstörda
        for (int[] position : ship.getPositions()) {
            Coordinate part = getCoordinate(position[0], position[1]);
            if (part != null && part.isShip()) {
                return false; // Om någon del av skeppet fortfarande är oskadad
            }
        }
        return true; // Alla delar av skeppet är förstörda
    }

    public boolean checkLost() {
        for (Coordinate coord : map) {
            if (coord.isShip()) {
                return false; // Det finns fortfarande skepp på kartan
            }
        }
        return true; // Alla skepp är förstörda
    }

    // METHOD FOR TESTING
    public void printMap() {
        System.out.println("UserMap:");
        for(Coordinate c : map) {
            System.out.println(c.getX() + " " + c.getY() + " " + c.isShip());
        }
    }
}
