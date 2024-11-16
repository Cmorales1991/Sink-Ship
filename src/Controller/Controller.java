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

        Platform.runLater(() -> {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            // Place ships in view
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    Coordinate coord = user.getMap().getCoordinate(x, y);
                    if (coord != null && coord.isShip()) {
                        System.out.println("Placing ship on: " + x + " ," + y);
                        view.updateMap(x, y, "s", user instanceof ServerUser);
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
                    handleIncomingMessage(server, message);
                    Thread.sleep(2000);
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
                    handleIncomingMessage(client, message);
                    Thread.sleep(2000);
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

    // WIP
    public void updateCoordinate(int x, int y, boolean isServer, boolean isHit) {
        String status;
        if (isHit) {
            status = "h";
        }
        else {
            status = "m";
        }
        view.updateMap(x, y, status, isServer);
    }

    private void handleIncomingMessage(User user, String message) {
        if (message.contains("shot")) {

            String[] parts = message.split(" ");
            String type = parts[0]; // denna kanske inte behövs

            Attack takenAttack = reformatTakenAttack(parts[2]);
            user.takeAttack(takenAttack.getX(), takenAttack.getY());

            boolean wasHit = user.getMap().getCoordinate(takenAttack.getX(), takenAttack.getY()).isDestroyed() &&
                    user.getMap().getCoordinate(takenAttack.getX(), takenAttack.getY()).isShip();
            String result = wasHit ? "h" : "m";

            if (wasHit && user.getMap().checkIfShipSunk(takenAttack.getX(), takenAttack.getY())) {
                result = "s";
            }

            // funkar ej härifrån heller
            // view.updateMap(4,4, "s", true);

            Attack nextAttack = user.performAttack();
            String nextAttackMessage = createMessage(result, reformatSentAttack(nextAttack));

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
