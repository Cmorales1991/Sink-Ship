package Model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerUser extends User
{

    final static int PORT = 6667;

    private ServerSocket serverSocket;
    private BufferedReader input;
    private PrintWriter output;

    public ServerUser(UserMap map)
    {
        super(map);
    }

    public void initialize()
    {
        try
        {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server starting on port: " + PORT);
            System.out.println("Waiting for Client to connect...");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        Thread thread = new Thread(()-> {

            try
            {
                Socket client = serverSocket.accept();
                System.out.println("Client has connected");

                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

                handleClient(client);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        });
        thread.setName("Waiting for Client thread");
        thread.start();

    }

    public void sendMessageToClient(String message)
    {
        try
        {
            output.println(message);
            System.out.println("Message sent to CLIENT: " + message);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket socket) throws IOException
    {
        new Thread(()-> {

           String messageFromClient = null;
            do
            {
                try
                {
                    messageFromClient = input.readLine();
                    System.out.println("Message from CLIENT: " + messageFromClient);
                    lastMessageReceived = messageFromClient;
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }

                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }

            }while (!messageFromClient.equals("game over"));


            //Om vi kommer hit så kommer server stängas av och då stänger vi av klienten först.
            try
            {
                if(socket != null)
                {
                    socket.close();
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public String getLastMessageReceived()
    {
        return lastMessageReceived;
    }
}
