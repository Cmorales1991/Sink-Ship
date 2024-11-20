package View;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ViewMenu extends View {

    private final View nextView;
    private final Stage primaryStage;
    private ViewGame viewGame;

    private UserChoice userChoice;

    public ViewMenu(int height, int width, Stage primaryStage) {
        super(height, width);
        this.primaryStage = primaryStage;
        nextView = null;
        userChoice = UserChoice.NO_PICK;
        init(true);
    }

    @Override
    protected void init(boolean isServer) {
        // Server button
        Button serverButton = new Button("Play as Server");
        serverButton.setPrefSize(150, 50);
        serverButton.setLayoutX((width / 2) - 75);
        serverButton.setLayoutY((height / 2) - 60);

        // Client button
        Button clientButton = new Button("Play as Client");
        clientButton.setPrefSize(150, 50);
        clientButton.setLayoutX((width / 2) - 75);
        clientButton.setLayoutY((height / 2));

        // Server start
        serverButton.setOnAction(event -> {
            System.out.println("Starting game as Server");
            if (viewGame == null) {
                userChoice = UserChoice.SERVER;
                viewGame = new ViewGame(height, width, true);
                primaryStage.setScene(viewGame.getScene());
                setTitle("Battleship - Server");
            } else {
                userChoice = UserChoice.SERVER;
                primaryStage.setScene(viewGame.getScene());
                setTitle("Battleship - Server");
            }
        });

        // Client start
        clientButton.setOnAction(event -> {
            System.out.println("Starting game as Client");
            if (viewGame == null) {
                userChoice = UserChoice.CLIENT;
                viewGame = new ViewGame(height, width, true);
                primaryStage.setScene(viewGame.getScene());
                setTitle("Battleship - Client");
            } else {
                userChoice = UserChoice.CLIENT;
                primaryStage.setScene(viewGame.getScene());
                setTitle("Battleship - Client");
            }
        });

        // Exit button
        Button exitButton = new Button("Quit");
        exitButton.setPrefSize(150, 50);
        exitButton.setLayoutX((width / 2) - 75);
        exitButton.setLayoutY((height / 2) + 60);
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
