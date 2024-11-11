package View;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ViewMenu extends View {

    private View nextView;
    private Stage primaryStage;
    private ViewGame viewGameServer;
    private ViewGame viewGameClient;

    public ViewMenu(int height, int width, Stage primaryStage) {
        super(height, width);
        this.primaryStage = primaryStage;
        nextView = null;
        new Thread(() -> {
        viewGameServer = new ViewGame(height, width, true);
        viewGameClient = new ViewGame(height, width, false);
        }).start();
        init(true);
    }

    @Override
    protected void init(boolean isServer) {
        // serverstart knapp
        Button serverButton = new Button("Starta som Server");
        serverButton.setPrefSize(150, 50);
        serverButton.setLayoutX((width / 2) - 75);
        serverButton.setLayoutY((height / 2) - 60);
        // serverstartar när man klickar på start
        serverButton.setOnAction(event -> startServerView());

        // klientstart knapp
        Button clientButton = new Button("Starta som Klient");
        clientButton.setPrefSize(150, 50);
        clientButton.setLayoutX((width / 2) - 75);
        clientButton.setLayoutY((height / 2));
        // klientserver startar när man klickar på start
       clientButton.setOnAction(event -> startClientView());

        // avsluta knapp
        Button exitButton = new Button("Avsluta");
        exitButton.setPrefSize(150, 50);
        exitButton.setLayoutX((width / 2) - 75);
        exitButton.setLayoutY((height / 2) + 60);
        // exitbutton svarar vid tryck och avslutar program
        exitButton.setOnAction(event -> {
            primaryStage.close();
        });

        pane.getChildren().addAll(serverButton, clientButton, exitButton);
    }

    public void startServerView() {
        if (viewGameServer == null) {
            new Thread(() -> {
                viewGameServer = new ViewGame(height, width, true);
                Platform.runLater(() -> showGameView(viewGameServer, "Sänka Skepp - Server"));
            }).start();
        } else {
            showGameView(viewGameServer, "Sänka Skepp - Server");
        }
    }

    public void startClientView() {
        if (viewGameClient == null) {
            new Thread(() -> {
                viewGameClient = new ViewGame(height, width, false);
                Platform.runLater(() -> showGameView(viewGameClient, "Sänka Skepp - Klient"));
            }).start();
        } else {
            showGameView(viewGameClient, "Sänka Skepp - Klient");
        }
    }

    private void showGameView(ViewGame viewGame, String title) {
        primaryStage.setScene(viewGame.getScene());
        setTitle(title);
    }

    private void setTitle(String title) {
        primaryStage.setTitle(title);
    }

    @Override
    public View update() {
        return nextView;
    }
}
