package Controller;

import Model.Attack;
import Model.ClientUser;
import Model.ServerUser;
import Model.User;
import View.ViewGame;

public class Controller {

    private User user;
    private ViewGame view;
    private boolean runGame = true;

    public Controller(User user, ViewGame view) {
        this.user = user;
        this.view = view;
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

    private String createString(String code, String coordinate) {
        return code + " shot " + coordinate;
    }

    public void startGame() {
        if (user instanceof ServerUser) {
            ServerUser server = (ServerUser) user;
            server.initialize();
            runServerGameLoop(server);
        } else if (user instanceof ClientUser) {
            ClientUser client = (ClientUser) user;
            client.initialize("localhost", 6667);

            // Anropa createString med "i" för att indikera att klienten skjuter första skottet
            String firstAttackMessage = createString("i", reformatSentAttack(user.performAttack()));
            client.sendMessage(firstAttackMessage);

            runClientGameLoop(client);
        }
    }

    private void runServerGameLoop(ServerUser server) {
        while (runGame) {
            if (user.checkLost()) {
                System.out.println("ServerPlayer lost!");
                server.sendMessageToClient("game over");
                runGame = false;
            } else {
                try {
                    String message = server.getLastMessageReceived();
                    if (message != null) {
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
            if (user.checkLost()) {
                System.out.println("ClientPlayer lost!");
                client.sendMessage("game over");
                runGame = false;
            } else {
                try {
                    String message = client.getLastMessageReceived();
                    if (message != null) {
                        handleIncomingMessage(client, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleIncomingMessage(User user, String message) {
        if (message.contains("shot")) {
            Attack takenAttack = reformatTakenAttack(message.split(" ")[2]);
            user.takeAttack(takenAttack.getX(), takenAttack.getY());

            // Kontrollera resultatet av motståndarens skott
            boolean isHit = user.getMap().checkHit(takenAttack.getX(), takenAttack.getY());
            boolean isSunk = user.getMap().checkSunk(takenAttack.getX(), takenAttack.getY());
            boolean isGameOver = user.getMap().checkLost();

            // Välj rätt kod baserat på resultatet av skottet
            String code;
            if (isGameOver) {
                code = "game over";
                System.out.println("Game Over, stopping game.");
                runGame = false;
            } else {
                if (isSunk) {
                    code = "s"; // Sänkt skepp
                } else if (isHit) {
                    code = "h"; // Träff
                } else {
                    code = "m"; // Miss
                }

                // Uppdatera vyn med resultatet av motståndarens skott
                view.updateMap(takenAttack.getX(), takenAttack.getY(), isHit ? "hit" : "miss");
                view.update();

                // Skapa nästa attack och meddelande att skicka
                Attack nextAttack = user.performAttack();
                String nextAttackMessage = createString(code, reformatSentAttack(nextAttack));

                // Skicka nästa attackmeddelande till motståndaren
                if (user instanceof ServerUser) {
                    ((ServerUser) user).sendMessageToClient(nextAttackMessage);
                } else if (user instanceof ClientUser) {
                    ((ClientUser) user).sendMessage(nextAttackMessage);
                }
            }
        } else if (message.equals("game over")) {
            System.out.println("Game Over, stopping game.");
            runGame = false;
        }
    }

}
