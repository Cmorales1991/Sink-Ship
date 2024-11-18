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
    private final Object lock = new Object();

    public ClientUser(UserMap map)
    {
        super(map);
    }

    public void initialize(String host, int port) {
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Client connected to " + host + ":" + port);

            Thread thread = new Thread(()->{
                try {
                    handleServer();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setName("Client thread");
            thread.start();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToServer(String message) {
        try {
            this.lastMessageSent = message;
            output.println(message);
            System.out.println("Message sent to Server: " + message);
        }
        catch (Exception e) {
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
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while(!(messageFromServer.equals("game over")));

        try {
            sendMessageToServer("game over");
            socket.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastMessageReceived() {
        synchronized (lock) {
            return lastMessageReceived;
        }
    }

    public String getLastMessageSent() {
        synchronized (lock) {
            return lastMessageSent;
        }
    }
}