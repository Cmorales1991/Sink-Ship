package Model;

public class Coordinate {

    private int x;
    private int y;
    private boolean ship;
    private Ship shipReference;

    public Coordinate(int x, int y, boolean ship) {
        this.x = x;
        this.y = y;
        this.ship = ship;
        this.shipReference = null;
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

    public void setShipReference(Ship shipReference) {
        this.shipReference = shipReference;
    }

    public Ship getShipReference() {
        return shipReference;
    }

    public void destroyShip() {
        this.ship = false;
    }
}
