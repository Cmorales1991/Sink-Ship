import Controller.Controller;
import Model.ClientUser;
import Model.ServerUser;
import Model.UserMap;
import View.UserChoice;
import View.ViewGame;
import View.ViewMenu;
import javafx.application.Application;
import javafx.stage.Stage;
import Model.Coordinate;

public class Main extends Application {
    public static void main(String[] args) {

        /* Randomly generated UserMap to pass as argument to either ClientUser or ServerUser,
        depending on the user's choice. */
        UserMap map = new UserMap();

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
    public void start(Stage primaryStage) {
        
        ViewMenu viewMenu = new ViewMenu(500, 800, primaryStage);
        primaryStage.setScene(viewMenu.getScene());
        primaryStage.setTitle("Sänka Skepp");
        primaryStage.show();

        new Thread(() ->
        {
            UserChoice userInput;

            do {
                userInput = viewMenu.getUserChoice();

                //System.out.println(userInput);

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

            } while (userInput == UserChoice.NO_PICK);

        }).start();
    }
}
