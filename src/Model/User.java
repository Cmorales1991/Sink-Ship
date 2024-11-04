package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class User {

    private Map map;
    private List<Coordinate> attackedCoordinates = new ArrayList<Coordinate>();

    public User(Map map) {
        this.map = map;
    }

    public void attack() {
        // attackera slumpad koordinat, gå igenom attackedCoordinates och prova
        // igen om koordinaten redan attackerats,
        // när en koordinat blivit attackerad, lägg den i attackedCoordinates
    }
}
