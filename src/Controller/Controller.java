package Controller;

import Model.*;
import View.ViewGame;
import javafx.application.Platform;

import java.util.List;
import java.util.Objects;

public class Controller {

    private User user;
    private ViewGame view;
    private boolean runGame = true;

    public Controller(User user, ViewGame view) {
        this.user = user;
        this.view = view;

        Platform.runLater(() -> {
            // Place ships in view
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    Coordinate coord = user.getMap().getCoordinate(x, y);
                    if (coord != null && coord.isShip()) {
                        view.placeShips(x, y, user instanceof ServerUser);
                    }
                }
            }
        });
    }

    private String reformatSentAttack(Attack attack) {
        String formattedAttack = String.valueOf(attack.getX());

        switch (attack.getY()) {
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
            default:
                throw new IllegalArgumentException("Invalid Y coordinate: " + attack.getY());
        }
        return formattedAttack;
    }

    private Attack reformatTakenAttack(String attack) {
        int x = Character.getNumericValue(attack.charAt(0));
        int y;

        switch (attack.charAt(1)) {
            case 'a':
                y = 0;
                break;
            case 'b':
                y = 1;
                break;
            case 'c':
                y = 2;
                break;
            case 'd':
                y = 3;
                break;
            case 'e':
                y = 4;
                break;
            case 'f':
                y = 5;
                break;
            case 'g':
                y = 6;
                break;
            case 'h':
                y = 7;
                break;
            case 'i':
                y = 8;
                break;
            case 'j':
                y = 9;
                break;
            default:
                throw new IllegalArgumentException("Invalid Y coordinate: " + attack.charAt(1));
        }
        return new Attack(x, y);
    }

    private String createMessage(String result, String coordinate) {
        return result + " shot " + coordinate;
    }

    public void startGame() {
        if (user instanceof ServerUser) {
            ServerUser server = (ServerUser) user;
            server.initialize();
            runServerGameLoop(server);
        } else if (user instanceof ClientUser) {
            ClientUser client = (ClientUser) user;
            client.initialize("localhost", 6667);
            Attack initAttack = user.performAttack();
            String initMessage = createMessage("i", reformatSentAttack(initAttack));
            client.sendMessageToServer(initMessage);
            runClientGameLoop(client);
        }
    }

    private void runServerGameLoop(ServerUser server) {
        while (runGame) {
            try {
                String message = server.getLastMessageReceived();
                if (message != null) {
                    if(message.equals("game over"))
                    {
                        System.out.println("Client declared game over. Server wins!");
                        runGame = false;
                        break;
                    }
                    handleIncomingMessage(server, message);
                    Thread.sleep(1500);
                }
                if (user.checkLost()) {
                    System.out.println("Server lost!");
                    server.sendMessageToClient("game over");
                    runGame = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Server game loop ended.");
    }

    private void runClientGameLoop(ClientUser client) {
        while (runGame) {
            try {
                String message = client.getLastMessageReceived();
                if (message != null) {
                    if(message.equals("game over"))
                    {
                        System.out.println("Server declared game over. Client wins!");
                        runGame = false;
                        break;
                    }
                    handleIncomingMessage(client, message);
                    Thread.sleep(1500);
                }
                if (user.checkLost()) {
                    System.out.println("Client lost!");
                    client.sendMessageToServer("game over");
                    runGame = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Client game loop ended.");
    }

    private void handleIncomingMessage(User user, String message) {
        if (message.contains("shot")) {
            String[] parts = message.split(" ");
            String previousAttackResult = parts[0];
            String previousAttackCoordinate; // Coord of previous attack

            // Skapa ett Attack-objekt baserat på inkommande koordinater
            Attack takenAttack = reformatTakenAttack(parts[2]);
            int x = takenAttack.getX();
            int y = takenAttack.getY();

            // Utför attacken på användarens karta och kontrollera resultatet
            user.takeAttack(x, y);

            // Hämtar aktuell koordinat för att kontrollera om det är ett skepp och om det har förstörts
            Coordinate targetCoordinate = user.getMap().getCoordinate(x, y);
            boolean wasHit = targetCoordinate.isShip() && targetCoordinate.isDestroyed();

            // Hantera miss eller träff, och kontrollera om hela skeppet är sänkt
            if (wasHit) {
                // Kontrollera om hela skeppet är sänkt
                boolean isShipSunk = user.getMap().checkIfShipSunk(x, y);
                if (isShipSunk) {
                    // Uppdatera alla delar av det sänkta skeppet till "s"
                    for (Coordinate coord : user.getMap().getSunkShipCoordinates(x, y)) {
                        view.updateMaps(coord.getX(), coord.getY(), "s", user instanceof ServerUser);
                    }
                    System.out.println("A ship has been sunk!");
                } else {
                    view.updateMaps(x, y, "h", user instanceof ServerUser); // "h" för träff
                }
            } else {
                view.updateMaps(x, y, "m", user instanceof ServerUser); // "m" för miss
            }

            // Uppdatera fiendens karta med resultatet av föregående attack
            if (user.getLastMessageSent() != null) {
                previousAttackCoordinate = user.getLastMessageSent().split(" ")[2];
                int prevX = reformatTakenAttack(previousAttackCoordinate).getX();
                int prevY = reformatTakenAttack(previousAttackCoordinate).getY();

                if (previousAttackResult.equals("m")) {
                    view.updateMaps(prevX, prevY, "m", !(user instanceof ServerUser));
                } else if (previousAttackResult.equals("h")) {
                    view.updateMaps(prevX, prevY, "h", !(user instanceof ServerUser));
                }
            }

            // Vänta på motståndarens tur
            user.waitForOpponentTurn();

            // Förbered nästa attack och skicka meddelande till motståndaren
            Attack nextAttack = user.performAttack();
            String nextAttackMessage = createMessage(wasHit ? "h" : "m", reformatSentAttack(nextAttack));

            if (user instanceof ServerUser) {
                ((ServerUser) user).sendMessageToClient(nextAttackMessage);
            } else if (user instanceof ClientUser) {
                ((ClientUser) user).sendMessageToServer(nextAttackMessage);
            }

        } else if (message.contains("game over")) {
            System.out.println("Game won!");
            runGame = false;
        }
    }


}