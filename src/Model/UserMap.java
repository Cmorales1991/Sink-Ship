package Model;

import java.util.Random;

public class UserMap extends Map {

    private final int MAP_SIZE = 10;
    private final int[] SHIPS = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    public UserMap() {
        super();
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

    private Coordinate getCoordinate(int x, int y) {
        return map.stream()
                .filter(coord -> coord.getX() == x && coord.getY() == y)
                .findFirst()
                .orElse(null);
    }

    // METHOD FOR TESTING
    @Override
    public void printMap() {
        System.out.println("UserMap:");
        for(Coordinate c : map) {
            System.out.println(c.getX() + " " + c.getY() + " " + c.isShip());
        }
    }
}
