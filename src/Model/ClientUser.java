package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientUser extends User
{

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private String lastMessageReceived;

    public ClientUser(UserMap map)
    {
        super(map);
    }

    public void initialize(String host, int port)
    {
        try
        {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Anslutit till " + host + ":" + port);

            Thread thread = new Thread(()->{

                try
                {
                    handleServer();
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            });

            thread.setName("Client thread");
            thread.start();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message)
    {
        try
        {
            output.println(message);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void handleServer() throws InterruptedException {
        String messageFromServer = null;

        do
        {
            try
            {
                messageFromServer = input.readLine();
                System.out.println("Message from Server: " + messageFromServer);
                lastMessageReceived = messageFromServer;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            Thread.sleep(2000);

        }while(!messageFromServer.equals("game over"));

        try
        {
            sendMessage("game over");
            socket.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getLastMessageReceived()
    {
        return lastMessageReceived;
    }
}