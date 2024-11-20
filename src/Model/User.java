package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class User {

    private final UserMap map;
    private final List<Attack> attacks = new ArrayList<>();
    protected String lastMessageReceived;
    protected String lastMessageSent;

    public User(UserMap map) {
        this.map = map;
    }

    // An attack will be sent to enemy player through Controller
    public Attack performAttack() {

        Attack performedAttack;
        Random rand = new Random();

        while (true) { // Loops until new Attack is generated, no duplicates allowed

            int x = rand.nextInt(10);
            int y = rand.nextInt(10);

            boolean exists = attacks.stream() // Check if coordinate has been attacked before
                    .anyMatch(attack -> attack.getX() == x && attack.getY() == y);

            if (!exists) { // If the new Attack is unregistered
                performedAttack = new Attack(x, y);
                attacks.add(performedAttack);
                return performedAttack;
            }
        }
    }

    public void takeAttack(int x, int y) {
        map.takeAttack(x,y);
    }

    public boolean checkLost() {
        return map.checkLost();
    }

    public UserMap getMap() {
        return map;
    }

    public String getLastMessageReceived() {
        return lastMessageReceived;
    }

    public String getLastMessageSent() {
        return lastMessageSent;
    }
}
