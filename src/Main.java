import Model.ClientUser;
import Model.ServerUser;
import Model.UserMap;
import View.ViewMenu;
import javafx.application.Application;
import javafx.stage.Stage;

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
    }
}
