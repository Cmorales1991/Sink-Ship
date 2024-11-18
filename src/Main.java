import Controller.Controller;
import Model.ClientUser;
import Model.ServerUser;
import Model.UserMap;
import View.UserChoice;
import View.ViewGame;
import View.ViewMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    @Override
    public void start(Stage primaryStage) {
        // Skapa huvudmenyn och sätt scenen till den
        ViewMenu viewMenu = new ViewMenu(500, 800, primaryStage);

        Platform.runLater(() -> {
            primaryStage.setScene(viewMenu.getScene());
            primaryStage.show();
        });
        primaryStage.setTitle("Sänka Skepp");


        // Lyssna på när användaren stänger fönstret
        primaryStage.setOnCloseRequest(event -> {
            shutdown.set(true);
        });

        // Skapa och starta en tråd som hanterar menyn och spelet
        Thread thread = new Thread(() -> {
            System.out.println("Spelet startas i tråden.");
            UserChoice userInput;

            do {
                System.out.println("Inne i tråden, börja kolla användarens val...");
                // Hämta användarens val från menyn
                userInput = viewMenu.getUserChoice();
                System.out.println("Vald användartyp: " + userInput);
                // Skapa spelplan och användare beroende på val
                if (userInput == UserChoice.SERVER) {
                    System.out.println("Server valdes.");
                    UserMap map = new UserMap();
                    ServerUser server = new ServerUser(map);
                    ViewGame viewGame = new ViewGame(500, 800, true);
                    Controller controller = new Controller(server, viewGame);
                    controller.startGame();  // Starta spelet för servern
                    break;
                } else if (userInput == UserChoice.CLIENT) {
                    System.out.println("Klient valdes.");
                    UserMap map = new UserMap();
                    ClientUser client = new ClientUser(map);
                    ViewGame viewGame = new ViewGame(500, 800, false);
                    Controller controller = new Controller(client, viewGame);
                    controller.startGame();  // Starta spelet för klienten
                    break;
                }


                // Vänta innan vi kollar om användaren har gjort ett val
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (!shutdown.get());  // Fortsätt tills shutdown är satt till true
            System.out.println("Slut på trådlogik.");
        });

        thread.setName("Menu thread");
        thread.setDaemon(true);  // Tråden stängs automatiskt när applikationen stänger
        thread.start();
    }

    @Override
    public void stop() {
        shutdown.set(true);  // Stänger ner tråden när applikationen stängs
    }

    public static void main(String[] args) {
        launch(args);  // Starta JavaFX-applikationen
    }
}
