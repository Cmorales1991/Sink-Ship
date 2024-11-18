package Model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerUser extends User {

    final static int PORT = 6667;

    private ServerSocket serverSocket;
    private BufferedReader input;
    private PrintWriter output;

    private String lastMessageReceived;
    private boolean isServerTurn = true;  // För att kontrollera om det är serverns tur

    public ServerUser(UserMap map) {
        super(map);
    }

    public void initialize() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server startat i port: " + PORT);
            System.out.println("Väntar på att klienten ansluter...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread thread = new Thread(() -> {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Klienten har anslutit");

                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

                handleClient(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setName("Waiting for Client thread");
        thread.start();
    }

    public synchronized void sendMessageToClient(String message) {
        try {
            output.println(message);
            System.out.println("Message sent to client: " + message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket socket) throws IOException {
        new Thread(() -> {
            String messageFromClient = null;
            do {
                try {
                    messageFromClient = input.readLine();
                    System.out.println("Message from Client: " + messageFromClient);
                    lastMessageReceived = messageFromClient;

                    if (messageFromClient.contains("shot")) {
                        handleAttackFromClient(messageFromClient);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Thread.sleep(2000); // Delay between processing messages
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } while (!messageFromClient.equals("game over"));

            // Om vi kommer hit så stänger vi servern och klienten
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public synchronized void handleAttackFromClient(String message) {
        // Servern väntar på att klienten skickar sitt drag
        while (!isServerTurn) {
            try {
                wait();  // Vänta tills det är serverns tur
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Hantera klientens attack
        Attack takenAttack = reformatTakenAttack(message.split(" ")[2]);
        boolean isHit = getMap().checkHit(takenAttack.getX(), takenAttack.getY());

        // Uppdatera UI eller logik här...

        // Skicka nästa drag till klienten
        sendMessageToClient("shot " + takenAttack.getX() + takenAttack.getY());

        // Servern måste vänta på klienten igen innan den kan göra sitt drag
        isServerTurn = false;
        notifyAll();  // Signalera att det är klientens tur

        // Nu kan servern hantera sitt eget drag efter att klienten gjort sitt
        handleServerTurn(); // Anropa här för att göra serverns drag
    }

    public synchronized void handleServerTurn() {
        // Vänta på att klienten skickar sitt skott
        while (isServerTurn) {
            try {
                wait();  // Vänta på att klienten gör sitt drag
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Servern skickar sin attack till klienten
        Attack nextAttack = performAttack();
        sendMessageToClient("shot " + nextAttack.getX() + nextAttack.getY());

        // Efter att servern har skickat sitt skott, signalera att det är klientens tur
        isServerTurn = true;
        notifyAll();  // Klienten kan nu göra sitt drag

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

