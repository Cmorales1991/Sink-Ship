package View;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ViewMenu extends View {

    private View nextView;
    private Stage primaryStage;
    private ViewGame viewGameServer;
    private ViewGame viewGameClient;


    private UserChoice userChoice;

    public ViewMenu(int height, int width, Stage primaryStage) {
        super(height, width);
        this.primaryStage = primaryStage;
        nextView = null;
        userChoice = UserChoice.NO_PICK;
        new Thread(() -> {
        viewGameServer = new ViewGame(height, width, true);
        viewGameClient = new ViewGame(height, width, false);
        }).start();
        init(true);
    }

    @Override
    protected void init(boolean isServer) {
        // serverstart knapp
        Button serverButton = new Button("Play as Server");
        serverButton.setPrefSize(150, 50);
        serverButton.setLayoutX((width / 2) - 75);
        serverButton.setLayoutY((height / 2) - 60);

        // serverstartar när man klickar på start
        serverButton.setOnAction(event -> {
            System.out.println("Starting game as Server");
            if (viewGameServer == null) {
                userChoice = UserChoice.SERVER;
                new Thread(() -> {
                    viewGameServer = new ViewGame(height, width, true);
                    Platform.runLater(() -> {
                        primaryStage.setScene(viewGameServer.getScene());
                        setTitle("Battleship - Server");  // ändrar titeln när server start väljs
                    });
                }).start();
            } else {
                userChoice = UserChoice.SERVER;
                primaryStage.setScene(viewGameServer.getScene());
                setTitle("Battleship - Server");  // ändrar titeln när server start väljs
            }
        });

        // klientstart knapp
        Button clientButton = new Button("Play as Client");
        clientButton.setPrefSize(150, 50);
        clientButton.setLayoutX((width / 2) - 75);
        clientButton.setLayoutY((height / 2));

        // klientserver startar när man klickar på start
        clientButton.setOnAction(event -> {
            System.out.println("Starting game as Client");
            if (viewGameClient == null) {
                userChoice = UserChoice.CLIENT;
                new Thread(() -> {
                    viewGameClient = new ViewGame(height, width, true);
                    Platform.runLater(() -> {
                        primaryStage.setScene(viewGameServer.getScene());
                        setTitle("Battleship - Client");  // ändrar titeln när klient start väljs
                    });
                }).start();
            } else {
                userChoice = UserChoice.CLIENT;
                primaryStage.setScene(viewGameClient.getScene());
                setTitle("Battleship - Client");  // ändrar titeln när klient start väljs
            }
        });

        // avsluta knapp
        Button exitButton = new Button("Quit");
        exitButton.setPrefSize(150, 50);
        exitButton.setLayoutX((width / 2) - 75);
        exitButton.setLayoutY((height / 2) + 60);
        // exitbutton svarar vid tryck och avslutar program
        exitButton.setOnAction(event -> {
            primaryStage.close();
        });

        pane.getChildren().addAll(serverButton, clientButton, exitButton);
    }

    private void setTitle(String title) {
        primaryStage.setTitle(title);
    }

    @Override
    public View update() {
        return nextView;
    }

    public UserChoice getUserChoice()
    {
        return userChoice;
    }
}
