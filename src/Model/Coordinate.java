package Model;

public class Coordinate {

    private int x;
    private int y;
    private boolean ship;

    public Coordinate(int x, int y, boolean ship) {
        this.x = x;
        this.y = y;
        this.ship = ship;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isShip() {
        return ship;
    }

    public void setShip(boolean ship) {
        this.ship = ship;
    }
}
