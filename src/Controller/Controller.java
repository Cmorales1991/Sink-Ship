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

    private String createMessage(String code, Attack attack) {
        String formattedAttack = attack.getX() + "" + (char) ('a' + attack.getY());
        return code + " shot " + formattedAttack;
    }

    private Attack parseMessage(String message) {
        String[] parts = message.split(" ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid message format: " + message);
        }

        int x = Character.getNumericValue(parts[2].charAt(0));
        int y = parts[2].charAt(1) - 'a';
        return new Attack(x, y);
    }

    public void startGame() {
        if (user instanceof ServerUser) {
            ServerUser server = (ServerUser) user;
            server.initialize();
            runServerGameLoop(server);
        } else if (user instanceof ClientUser) {
            ClientUser client = (ClientUser) user;
            client.initialize("localhost", 6667);
            client.sendMessage(createMessage("i", user.performAttack()));
            runClientGameLoop(client);
        }
    }

    private void handleIncomingMessage(User user, String message) {
        if (message.equals("game over")) {
            runGame = false; // Avsluta spelet
            return;
        }

        String[] parts = message.split(" ");
        Attack takenAttack = parseMessage(message);

        // Uppdatera kartan med attackresultat
        boolean hit = user.getMap().wasHit(takenAttack.getX(), takenAttack.getY());
        boolean shipSunk = user.getMap().isShipSunk(takenAttack.getX(), takenAttack.getY());

        user.takeAttack(takenAttack.getX(), takenAttack.getY());

        // Kontrollera om spelet är slut
        boolean lost = user.checkLost();

        String responseCode;
        if (lost) {
            responseCode = "game over";
            runGame = false; // Stoppa spelet
        } else if (shipSunk) {
            responseCode = "s";
        } else if (hit) {
            responseCode = "h";
        } else {
            responseCode = "m";
        }

        if (!responseCode.equals("game over")) {
            // Utför nästa attack om spelet inte är slut
            Attack nextAttack = user.performAttack();
            String responseMessage = createMessage(responseCode, nextAttack);

            if (user instanceof ServerUser) {
                ((ServerUser) user).sendMessageToClient(responseMessage);
            } else if (user instanceof ClientUser) {
                ((ClientUser) user).sendMessage(responseMessage);
            }

            view.updateMap(takenAttack.getX(), takenAttack.getY(), hit ? "hit" : "miss");
            view.update();
        } else {
            // Skicka "game over"
            if (user instanceof ServerUser) {
                ((ServerUser) user).sendMessageToClient("game over");
            } else if (user instanceof ClientUser) {
                ((ClientUser) user).sendMessage("game over");
            }
        }
    }

    private void runServerGameLoop(ServerUser server) {
        while (runGame) {
            String message = server.getLastMessageReceived();
            if (message != null) {
                handleIncomingMessage(server, message);
                server.setLastMessageReceived(null);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void runClientGameLoop(ClientUser client) {
        while (runGame) {
            String message = client.getLastMessageReceived();
            if (message != null) {
                handleIncomingMessage(client, message);
                client.setLastMessageReceived(null);
            }

            if (!runGame) {
                break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
