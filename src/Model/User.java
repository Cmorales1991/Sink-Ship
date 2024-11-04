package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class User {

    private Map map;
    private List<Attack> attacks = new ArrayList<>();

    public User(Map map) {
        this.map = map;
    }

    public void attack() {

        boolean attackedCoordinate = false;

        while (!attackedCoordinate) {
            Random rand = new Random();
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);

            for (Attack attack : attacks) {
                if (attack.getX() == x && attack.getY() == y) {
                    break;
                }
                else {
                    attacks.add(new Attack(x,y));
                    attackedCoordinate = true;
                }
            }
        }

        // attackera slumpad koordinat, gå igenom attackedCoordinates och prova
        // igen om koordinaten redan attackerats,
        // när en koordinat blivit attackerad, lägg den i attackedCoordinates
    }
}
