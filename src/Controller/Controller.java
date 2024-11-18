package Controller;

import Model.*;
import View.ViewGame;
import javafx.application.Platform;

public class Controller {

    private User user;
    private ViewGame view;
    private boolean runGame = true;

    public Controller(User user, ViewGame view) {
        this.user = user;
        this.view = view;
    }

    // Metod som omformaterar skickat attack (från koordinater till en läsbar sträng)
    private String reformatSentAttack(Attack attack) {
        String formattedAttack = String.valueOf(attack.getX());

        switch (attack.getY()) {
            case 0: formattedAttack += "a"; break;
            case 1: formattedAttack += "b"; break;
            case 2: formattedAttack += "c"; break;
            case 3: formattedAttack += "d"; break;
            case 4: formattedAttack += "e"; break;
            case 5: formattedAttack += "f"; break;
            case 6: formattedAttack += "g"; break;
            case 7: formattedAttack += "h"; break;
            case 8: formattedAttack += "i"; break;
            case 9: formattedAttack += "j"; break;
            default: throw new IllegalArgumentException("Invalid Y coordinate: " + attack.getY());
        }

        return formattedAttack;
    }

    // Metod som omformaterar mottagen attack (från sträng till koordinater)
    private Attack reformatTakenAttack(String attack) {
        int x = Character.getNumericValue(attack.charAt(0));
        int y;

        switch (attack.charAt(1)) {
            case 'a': y = 0; break;
            case 'b': y = 1; break;
            case 'c': y = 2; break;
            case 'd': y = 3; break;
            case 'e': y = 4; break;
            case 'f': y = 5; break;
            case 'g': y = 6; break;
            case 'h': y = 7; break;
            case 'i': y = 8; break;
            case 'j': y = 9; break;
            default: throw new IllegalArgumentException("Invalid Y coordinate: " + attack.charAt(1));
        }

        return new Attack(x, y);
    }

    // Metod som skapar en sträng för att skicka meddelande om attack
    private String createString(String code) {
        return code + " shot ";
    }

    // Starta spelet, hantera placering av skepp och skicka attacker
    public void startGame() {
        System.out.println("Startar spelet...");
        // Placera skeppen på spelplanen för både server och klient
        if (user instanceof ServerUser) {
            System.out.println("Server spelar...");
            ServerUser server = (ServerUser) user;
            server.getMap().placeShips(); // Placera skepp för servern
            initializeAndDisplayShips(server.getUserMap(), true);
            System.out.println("Försöker starta servern...");
            server.initialize();
            runServerGameLoop(server);
        } else if (user instanceof ClientUser) {
            System.out.println("Klient spelar...");
            ClientUser client = (ClientUser) user;
            client.getMap().placeShips(); // Placera skepp för klienten
            initializeAndDisplayShips(client.getUserMap(), false);
            System.out.println("Försöker starta klienten och ansluta...");
            client.initialize("localhost", 6667);
            client.sendMessage("ready"); // Skicka en "ready"-signal till servern
            runClientGameLoop(client);
        }
        System.out.println("Spelet borde vara igång nu.");
    }

    // Serverns spel-loop
    private void runServerGameLoop(ServerUser server) {
        while (runGame) {
            System.out.println("Server väntar på nästa steg...");
            if (server.getMap().checkLost()) {
                System.out.println("Server har förlorat!");
                server.sendMessageToClient("game over");
                runGame = false;
            } else {
                try {
                    String message = server.getLastMessageReceived();
                    if (message != null) {
                        System.out.println("Server mottog meddelande: " + message);
                        handleIncomingMessage(server, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void runClientGameLoop(ClientUser client) {
        while (runGame) {
            System.out.println("Klient väntar på nästa steg...");
            if (client.getMap().checkLost()) {
                System.out.println("Klient har förlorat!");
                client.sendMessage("game over");
                runGame = false;
            } else {
                try {
                    String message = client.getLastMessageReceived();
                    if (message != null) {
                        System.out.println("Klient mottog meddelande: " + message);
                        handleIncomingMessage(client, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Metod som hanterar inkommande attacker och skott
    private void handleIncomingMessage(User user, String message) {
        System.out.println("Received message: " + message);
        if (message.contains("shot")) {
            // Extrahera koordinaterna för skottet
            Attack takenAttack = reformatTakenAttack(message.split(" ")[2]);
            user.takeAttack(takenAttack.getX(), takenAttack.getY()); // Hantera skottet

            // Kontrollera om skottet träffade eller missade
            boolean isHit = user.getMap().checkHit(takenAttack.getX(), takenAttack.getY());
            String attackResult = isHit ? "hit" : "miss";
            boolean isServer = user instanceof ClientUser; // True om det är klientens spelplan
            Platform.runLater(() ->
                    view.updateMap(takenAttack.getX(), takenAttack.getY(), attackResult, isServer)); // Uppdatera UI med träff eller miss

            // Om en spelare har förlorat, avsluta spelet
            if (user.getMap().checkLost()) {
                System.out.println("Game Over, " + (user instanceof ServerUser ? "Server" : "Client") + " lost!");
                if (user instanceof ServerUser) {
                    System.out.println("Server skickar meddelande till klient: " + message);
                    ((ServerUser) user).sendMessageToClient("game over");
                } else if (user instanceof ClientUser) {
                    System.out.println("Klient skickar meddelande till server: " + message);
                    ((ClientUser) user).sendMessage("game over");
                }
                runGame = false;
                return;
            }

            // Skicka nästa attack till den andra spelaren
            Attack nextAttack = user.performAttack();
            String nextAttackMessage = createString(reformatSentAttack(nextAttack));
            if (user instanceof ServerUser) {
                ((ServerUser) user).sendMessageToClient(nextAttackMessage);
            } else if (user instanceof ClientUser) {
                ((ClientUser) user).sendMessage(nextAttackMessage);
            }
        } else if (message.equals("game over")) {
            System.out.println("Game Over, stopping game.");
            runGame = false;
        }
    }

    // Metod för att visa skepp på spelplanen
    public void initializeAndDisplayShips(UserMap userMap, boolean isClientMap) {
        // Gå igenom alla koordinater och visa skepp på spelplanen
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Coordinate coord = userMap.getCoordinate(x, y);
                if (coord != null && coord.isShip()) {
                    String status = "ship"; // För att visa ett skepp
                    int finalX = x;
                    int finalY = y;
                    Platform.runLater(() ->
                            view.updateMap(finalX, finalY, status, isClientMap)); // Uppdatera GUI i JavaFX-tråden
                }
            }
        }
    }

    // Metod för att hantera träff/miss
    public void handleAttack(int x, int y, boolean isClientMap) {
        if (user.getMap().checkLost()) {
            System.out.println("Game Over, " + (user instanceof ServerUser ? "Server" : "Client") + " lost!");
            if (user instanceof ServerUser) {
                ((ServerUser) user).sendMessageToClient("game over");
            } else if (user instanceof ClientUser) {
                ((ClientUser) user).sendMessage("game over");
            }
            runGame = false;
            return;
        }
    }

    // Metod för att kolla om användaren har förlorat
    public boolean checkLost() {
        return user.checkLost();
    }
}