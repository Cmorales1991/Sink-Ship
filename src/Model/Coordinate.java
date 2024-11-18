package Model;

public class Coordinate {

    private int x;
    private int y;
    private boolean ship;
    private boolean destroyed;

    public Coordinate(int x, int y, boolean ship, boolean destroyed) {
        this.x = x;
        this.y = y;
        this.ship = ship;
        this.destroyed = destroyed;
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

    public void destroyShip() {
        this.destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
