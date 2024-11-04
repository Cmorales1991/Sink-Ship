package Model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Map map;
    private ServerUser user1;
    private ClientUser user2;
    private List<Ship> ships;

    public Game(Map map, ServerUser user1, ClientUser user2, List<Ship> ships) {
        this.map = map;
        this.user1 = user1;
        this.user2 = user2;
        this.ships = ships;
    }


    //En lista för skepp .. välj placering osv för logiken. visste ej vart du vill ha den //christoffer
//    private List<Ship> createShips() {
//        List<Ship> ships = new ArrayList<>();
//        ships.add(new Ship("Hangarfartyg", 5, true, "/images/HangarShip5.png"));
//        ships.add(new Ship("Slagskepp 1", 4, true, "/images/FightShip4.png"));
//        ships.add(new Ship("Slagskepp 2", 4, true, "/images/FightShip4.png"));
//        ships.add(new Ship("Kryssare 1", 3, true, "/images/CruiseShip3.png"));
//        ships.add(new Ship("Kryssare 2", 3, true, "/images/CruiseShip3.png"));
//        ships.add(new Ship("Kryssare 3", 3, true, "/images/CruiseShip3.png"));
//        ships.add(new Ship("Ubåt 1", 2, true, "/images/submarine2.png"));
//        ships.add(new Ship("Ubåt 2", 2, true, "/images/submarine2.png"));
//        ships.add(new Ship("Ubåt 3", 2, true, "images/submarine2.png"));
//        ships.add(new Ship("Ubåt 4", 2, true, "images/submarine2.png"));
//        return ships;
//    }

}
