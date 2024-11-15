import Controller.Controller;
import Model.ClientUser;
import Model.ServerUser;
import Model.UserMap;
import View.UserChoice;
import View.ViewGame;
import View.ViewMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import Model.Coordinate;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application
{
    AtomicBoolean shutdown = new AtomicBoolean(false);

    public static void main(String[] args) {

        /* Randomly generated UserMap to pass as argument to either ClientUser or ServerUser,
        depending on the user's choice. */

        /* ServerUser server = new ServerUser(map);
        ClientUser client = new ClientUser(map);

        server.initialize();
        client.initialize("localhost", 6667); */

        /* om användaren trycker på Server så skapas en ServerUser, annars skapas en ClientUser, och
        de skickas till controllern såhär:
        (användaren trycker på clientuser)

        ClientUser user = new ClientUser(map);
        Controller controller = new Controller(user, view);
         */

        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        ViewMenu viewMenu = new ViewMenu(500, 800, primaryStage);

        primaryStage.setScene(viewMenu.getScene());
        primaryStage.show();
        primaryStage.setTitle("Sänka Skepp");

        primaryStage.setOnCloseRequest(event -> {
            shutdown.set(true);
        });

        Thread thread = new Thread(() -> {

            UserChoice userInput;

            do {
                userInput = viewMenu.getUserChoice();

                if (userInput == UserChoice.SERVER)
                {
                    UserMap map = new UserMap();
                    ServerUser server = new ServerUser(map);
                    Controller controller = new Controller(server, new ViewGame(500, 800, true));
                    controller.startGame();
                    break;
                }
                else if (userInput == UserChoice.CLIENT)
                {
                    UserMap map = new UserMap();
                    ClientUser client = new ClientUser(map);
                    Controller controller = new Controller(client, new ViewGame(500, 800, false));
                    controller.startGame();
                    break;
                }

                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }

            } while (!shutdown.get());
        });

        thread.setName("Menu thread");

        //Genom att sätta en tråds Daemon-variabel till true så kommer tråden automatiskt stängas av när programmet stängs av.

        thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void stop()
    {
        shutdown.set(true);
    }

}