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

    private String lastMessageReceived;

    public ServerUser(UserMap map)
    {
        super(map);
    }

    public void initialize()
    {
        try
        {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        new Thread(()-> {

            try
            {
                Socket client = serverSocket.accept();
                System.out.println("Client connected");

                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

                handleClient(client);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }).start();

    }

    public void sendMessageToClient(String message)
    {
        try
        {
            output.println(message);
            System.out.println("Message sent to client: " + message);
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
                    System.out.println("Message from Client: " + messageFromClient);
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
