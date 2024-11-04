package Model;

public class Game {

    private Map map;
    private ServerUser user1;
    private ClientUser user2;

    public Game(Map map, ServerUser user1, ClientUser user2) {
        this.map = map;
        this.user1 = user1;
        this.user2 = user2;
    }

    public void convertCoordinate() {
        // ändra 0,0 till A,0 osv
    }
}
