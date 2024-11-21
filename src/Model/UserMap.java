package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserMap {

    private final List<Coordinate> map = new ArrayList<>();
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

        for (int size : SHIPS) {
            boolean placed = false;
            // Loops through ships until all are placed
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
        // Check largest end coordinate
        int endX = horizontal ? startX + size - 1 : startX;
        int endY = horizontal ? startY : startY + size - 1;

        // Check if ship fits on map
        if (endX >= MAP_SIZE || endY >= MAP_SIZE) return false;

        // Check surrounding coordinates for ships
        for (int x = Math.max(0, startX - 1); x <= Math.min(MAP_SIZE - 1, endX + 1); x++) {
            for (int y = Math.max(0, startY - 1); y <= Math.min(MAP_SIZE - 1, endY + 1); y++) {
                Coordinate coord = getCoordinate(x, y);
                if (coord.isShip()) {
                    return false;
                }
            }
        }
        return true; // If ship placement is allowed
    }

    private void placeShip(int startX, int startY, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            int x = horizontal ? startX + i : startX; // If horizontal, extend X-axis
            int y = horizontal ? startY : startY + i; // If vertical, extend Y-axis
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
        }
    }

    public boolean checkLost() {
        boolean noShips = true;

        for (Coordinate coord : map) {
            if (coord.isShip() && !coord.isDestroyed()) {
                noShips = false; // If any ships are not destroyed, return false
                break;
            }
        }
        return noShips;
    }

    public boolean checkIfShipSunk(int x, int y) {
        Coordinate attackedCoordinate = getCoordinate(x, y);

        // First check that the coordinate is a ship
        if (attackedCoordinate == null || !attackedCoordinate.isShip()) {
            return false;
        }

        // Check if surrounding X-coordinates are parts of ship (if so, ship is horizontal)
        boolean leftCoordinate = (getCoordinate(x + 1, y) != null) && (getCoordinate(x + 1, y).isShip());
        boolean rightCoordinate = (getCoordinate(x - 1, y) != null) && (getCoordinate(x - 1, y).isShip());
        boolean horizontal = (leftCoordinate || rightCoordinate);

        List<Coordinate> shipParts = new ArrayList<>();

        // Collect all ship parts to shipParts
        if (horizontal) {
            for (int i = x; i >= 0 && getCoordinate(i, y) != null && getCoordinate(i, y).isShip(); i--) {
                shipParts.add(getCoordinate(i, y));
            }
            for (int i = x; i <= MAP_SIZE && getCoordinate(i, y) != null && getCoordinate(i, y).isShip(); i++) {
                shipParts.add(getCoordinate(i, y));
            }
        }
        else {
            for (int i = y; i >= 0 && getCoordinate(x, i) != null && getCoordinate(x, i).isShip(); i--) {
                shipParts.add(getCoordinate(x, i));
            }
            for (int i = y; i <= MAP_SIZE && getCoordinate(x, i) != null && getCoordinate(x, i).isShip(); i++) {
                shipParts.add(getCoordinate(x, i));
            }
        }

        // If isDestroyed true for all coordinates in shipParts, return true (ship is sunk)
        return shipParts.stream().allMatch(Coordinate::isDestroyed);
    }

    // Method for testing
    public void printMap() {
        System.out.println("UserMap:");
        for(Coordinate c : map) {
            System.out.println(c.getX() + " " + c.getY() + " " + c.isShip());
        }
    }
}
