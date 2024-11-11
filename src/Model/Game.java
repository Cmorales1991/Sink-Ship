package Model;

public class Game {

    private Map map;
    private User user; // depending on game instance, this will be either ServerUser or ClientUser

    public Game(Map map, User user) {
        this.map = map;
        this.user = user;
    }

    private String reformatSentAttack(Attack attack) {

        String formattedAttack = String.valueOf(attack.getX());

        switch(attack.getY()) {
            case 0:
                formattedAttack += "a";
                break;
            case 1:
                formattedAttack += "b";
                break;
            case 2:
                formattedAttack += "c";
                break;
            case 3:
                formattedAttack += "d";
                break;
            case 4:
                formattedAttack += "e";
                break;
            case 5:
                formattedAttack += "f";
                break;
            case 6:
                formattedAttack += "g";
                break;
            case 7:
                formattedAttack += "h";
                break;
            case 8:
                formattedAttack += "i";
                break;
            case 9:
                formattedAttack += "j";
                break;
        }
        return formattedAttack;
    }

    private Attack reformatTakenAttack(String attack) {
        Attack reformattedAttack = new Attack(1,1);
        return reformattedAttack;
    }

    private String createString(String coordinate) {
        String exempelString = "m shot " + coordinate;
        return exempelString;
    }

    public void startGame() {
        if (user instanceof ServerUser) {

        }
        if (user instanceof ClientUser) {

        }
    }

}
