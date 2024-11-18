package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class User {

    private UserMap map;
    private List<Attack> attacks = new ArrayList<>();
    protected String lastMessageReceived;
    protected String lastMessageSent;

    public User(UserMap map) {
        this.map = map;
    }

    // ATTACK WILL BE SENT TO ENEMY PLAYER (SERVER OR CLIENT) THROUGH GAME CLASS
    public Attack performAttack() {

        Attack performedAttack;
        Random rand = new Random();

        while (true) {

            int x = rand.nextInt(10);
            int y = rand.nextInt(10);

            boolean exists = attacks.stream()
                    .anyMatch(attack -> attack.getX() == x && attack.getY() == y);

            if (!exists) {
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
