package Model;

public class Coordinate {

    private int x;
    private int y;
    private boolean ship;
    private boolean destroyed; // Nytt fält för att spåra om skeppet är förstört

    public Coordinate(int x, int y, boolean ship) {
        this.x = x;
        this.y = y;
        this.ship = ship;
        this.destroyed = false; // Initiera som ej förstörd
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
        if (ship) {
            this.destroyed = true; // Markera koordinaten som förstörd om det är ett skepp
        }
    }

    public boolean isDestroyed() {
        return destroyed; // Returnerar om koordinaten är förstörd
    }
}
