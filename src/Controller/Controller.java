package Controller;

import Model.*;
import View.ViewGame;

public class Controller {

    private final User user;
    private final ViewGame view;
    private boolean runGame = true;

    public Controller(User user, ViewGame view) {
        this.user = user;
        this.view = view;

        // Place ships in view
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Coordinate coord = user.getMap().getCoordinate(x, y);
                if (coord != null && coord.isShip()) {
                    view.placeShips(x, y, user instanceof ServerUser);
                }
            }
        }
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
            // Perform first attack
            Attack initAttack = user.performAttack();
            client.sendMessageToServer(createMessage("i", reformatSentAttack(initAttack)));
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
                    Thread.sleep(1000);
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
                    Thread.sleep(1000);
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

    private void handleIncomingMessage(User user, String message) throws InterruptedException {
        if (message.contains("shot")) {

            String[] parts = message.split(" ");
            String previousAttackResult = parts[0];
            String previousAttackCoordinate; // Coordinate of previous attack

            Attack takenAttack = reformatTakenAttack(parts[2]);
            int x = takenAttack.getX();
            int y = takenAttack.getY();
            user.takeAttack(x, y);

            boolean wasHit = user.getMap().getCoordinate(takenAttack.getX(), takenAttack.getY()).isDestroyed() &&
                    user.getMap().getCoordinate(takenAttack.getX(), takenAttack.getY()).isShip();
            String resultTakenAttack = wasHit ? "h" : "m";

            if (wasHit && (user.getMap().checkIfShipSunk(x, y))) {
                resultTakenAttack = "s";
            }

            switch (resultTakenAttack) {
                case "h":
                        view.updateMap(takenAttack.getX(), takenAttack.getY(), "h", user instanceof ServerUser);
                    break;
                case "m":
                        view.updateMap(takenAttack.getX(), takenAttack.getY(), "m", user instanceof ServerUser);
                    break;
                case "s":
                        view.updateMap(takenAttack.getX(), takenAttack.getY(), "s", user instanceof ServerUser);
                    break;
            }

            // Print previous attack on enemy map
            if (user.getLastMessageSent() != null) {
                previousAttackCoordinate = user.getLastMessageSent().split(" ")[2];
                int previousAttackX = (reformatTakenAttack(previousAttackCoordinate)).getX();
                int previousAttackY = (reformatTakenAttack(previousAttackCoordinate)).getY();

                // Debug test
                // System.out.println(previousAttackResult + " on coordinates: " + previousAttackX + previousAttackY);

                // Print last attack's result to enemy map
                switch (previousAttackResult) {
                    case "m":
                        view.updateMap(previousAttackX, previousAttackY, "m", !(user instanceof ServerUser));
                        break;
                    case "h":
                        view.updateMap(previousAttackX, previousAttackY, "h", !(user instanceof ServerUser));
                        break;
                    case "s":
                        view.updateMap(previousAttackX, previousAttackY, "s", !(user instanceof ServerUser));
                        break;
                }
            }

            Attack nextAttack = user.performAttack();
            String nextAttackMessage = createMessage(resultTakenAttack, reformatSentAttack(nextAttack));

            // Decide where to send next attack message
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
