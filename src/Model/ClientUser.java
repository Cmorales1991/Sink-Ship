package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientUser extends User {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private String lastMessageReceived;
    private boolean isClientTurn = false;  // För att kontrollera om det är klientens tur

    public ClientUser(UserMap map) {
        super(map);
    }

    public void initialize(String host, int port) {
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Anslutit till " + host + ":" + port);

            Thread thread = new Thread(() -> {
                try {
                    handleServer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            thread.setName("Client thread");
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            output.println(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleServer() throws InterruptedException {
        String messageFromServer = null;

        do {
            try {
                messageFromServer = input.readLine();
                System.out.println("Message from Server: " + messageFromServer);
                lastMessageReceived = messageFromServer;

                if (messageFromServer.contains("shot")) {
                    handleAttackFromServer(messageFromServer);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Thread.sleep(2000);

        } while (!messageFromServer.equals("game over"));

        try {
            sendMessage("game over");
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void handleAttackFromServer(String message) {
        // Klienten väntar på att servern skickar sitt drag
        while (isClientTurn) {
            try {
                wait();  // Vänta på att det är klientens tur
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Hantera serverns attack
        Attack takenAttack = reformatTakenAttack(message.split(" ")[2]);
        boolean isHit = getMap().checkHit(takenAttack.getX(), takenAttack.getY());

        // Uppdatera UI eller logik här...

        // Skicka nästa drag till servern
        sendMessage("shot " + takenAttack.getX() + takenAttack.getY());

        // Klienten måste vänta på servern igen innan den kan göra sitt drag
        isClientTurn = true;
        notifyAll();  // Signalera att det är serverns tur

        // Nu kan klienten göra sitt drag
        handleClientTurn(); // Anropa här för att göra klientens drag
    }

    public synchronized void handleClientTurn() {
        // Vänta på att servern skickar sitt skott
        while (!isClientTurn) {
            try {
                wait();  // Vänta på att det är klientens tur
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Klienten skickar sin attack
        Attack nextAttack = performAttack();
        sendMessage("shot " + nextAttack.getX() + nextAttack.getY());

        // Efter att klienten har skickat sitt skott, signalera att det är serverns tur
        isClientTurn = false;
        notifyAll();  // Servern kan nu göra sitt drag
    }

    // Exempel på hur reformatTakenAttack kan se ut
    private Attack reformatTakenAttack(String coordinate) {
        // Här antar vi att koordinaten skickas som en sträng, t.ex. "A1", "B2" osv.
        // Vi omvandlar strängen till ett X, Y koordinatpar för att skapa en Attack.

        char x = coordinate.charAt(0);  // Få första bokstaven, t.ex. "A"
        int y = Integer.parseInt(coordinate.substring(1));  // Ta den numeriska delen, t.ex. "1"

        // Om vi har en referens till en Attack-klass, skapa ett nytt Attack-objekt.
        // Vi kan anta att Attack har en konstruktor som tar X och Y som argument.
        return new Attack(x, y);
    }

    public String getLastMessageReceived() {
        return lastMessageReceived;
    }

    public UserMap getUserMap() {
        return this.getMap();  // Anta att getMap() är en metod från superklassen User
    }
}
