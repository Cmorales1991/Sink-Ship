package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserMap {

    private List<Coordinate> map = new ArrayList<>();
    private final int MAP_SIZE = 10;
    private final int[] SHIPS = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    public UserMap() {
        // Create empty map, 10x10
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                map.add(new Coordinate(i, j, false, false));
            }
        }
        placeShips();
    }

    private void placeShips() {
        Random rand = new Random();
        // LOOP THROUGH ALL SHIPS
        for (int size : SHIPS) {
            boolean placed = false;
            // LOOPS UNTIL ALL SHIPS ARE PLACED
            while (!placed) {
                boolean horizontal = rand.nextBoolean();
                int startX = rand.nextInt(MAP_SIZE);
                int startY = rand.nextInt(MAP_SIZE);

                if (canPlaceShip(startX, startY, size, horizontal)) {
                    placeShip(startX, startY, size, horizontal);
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

    private void placeShip(int startX, int startY, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            int x = horizontal ? startX + i : startX;
            int y = horizontal ? startY : startY + i;
            getCoordinate(x, y).setShip(true);
        }
    }

    public Coordinate getCoordinate(int x, int y) {
        return map.stream()
                .filter(coord -> coord.getX() == x && coord.getY() == y)
                .findFirst()
                .orElse(null);
    }

    public void takeAttack(int x, int y) {
        Coordinate c = getCoordinate(x, y);
        if (c.isShip() && !c.isDestroyed()) {
            c.destroyShip();
            System.out.println("Part of ship was destroyed on coordinate (" + c.getX() + ", " + c.getY() + ")");
        }
        else {
            System.out.println("No ship found on coordinate (" + c.getX() + ", " + c.getY() + ")");
        }
    }

    public boolean checkLost() {
        boolean noShips = true;

        for (Coordinate coord : map) {
            if (coord.isShip() && !coord.isDestroyed()) {
                noShips = false; // If any ships are left and not destroyed, return false (still not lost)
                break;
            }
        }
        return noShips;
    }

    public boolean checkIfShipSunk(int x, int y) {
        for (Coordinate coord : map) {
            if (coord.isShip() && (coord.getX() == x && coord.getY() == y)) {
                return map.stream()
                        .filter(Coordinate::isShip)
                        .noneMatch(c -> c.getX() == x && c.getY() == y);
            }
        }
        return false;
    }

    // METHOD FOR TESTING
    public void printMap() {
        System.out.println("UserMap:");
        for(Coordinate c : map) {
            System.out.println(c.getX() + " " + c.getY() + " " + c.isShip());
        }
    }
}
