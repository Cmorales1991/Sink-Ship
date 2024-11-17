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

import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application
{
    AtomicBoolean shutdown = new AtomicBoolean(false);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ViewMenu viewMenu = new ViewMenu(500, 800, primaryStage);

        primaryStage.setScene(viewMenu.getScene());
        primaryStage.setTitle("Sänka Skepp");

        primaryStage.setOnCloseRequest(event -> {
            shutdown.set(true);
        });

        Thread thread = new Thread(() -> {
            UserChoice userInput;

            do {
                userInput = viewMenu.getUserChoice();
                if (userInput == UserChoice.SERVER) {
                    UserMap map = new UserMap();
                    ServerUser server = new ServerUser(map);
                    ViewGame viewGame = new ViewGame(500, 800, true);
                    Controller controller = new Controller(server, viewGame);

                    Platform.runLater(() -> {
                        primaryStage.setScene(viewGame.getScene());
                    });

                    controller.startGame();
                    break;
                }
                else if (userInput == UserChoice.CLIENT) {
                    UserMap map = new UserMap();
                    ClientUser client = new ClientUser(map);
                    ViewGame viewGame = new ViewGame(500, 800, false);
                    Controller controller = new Controller(client, viewGame);

                    Platform.runLater(() -> {
                        primaryStage.setScene(viewGame.getScene());
                    });

                    controller.startGame();
                    break;
                }
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            while (!shutdown.get());
        });

        thread.setName("Menu thread");

        //Genom att sätta en tråds Daemon-variabel till true så kommer tråden automatiskt stängas av när programmet stängs av.

        thread.setDaemon(true);
        thread.start();

        primaryStage.show();
    }

    @Override
    public void stop()
    {
        shutdown.set(true);
    }

}
