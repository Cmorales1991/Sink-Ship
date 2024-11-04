import Model.Map;
import View.GameMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private ToggleButton orientationToggle;

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();

        // spelbräda för server o klient
        GameMap playerMap = new GameMap(true);
        GameMap opponentMap = new GameMap(false);

        // positionering spelplan för serverplan och client
        AnchorPane playerPane = playerMap.getGameMapPane();
        playerPane.setLayoutX(50);
        playerPane.setLayoutY(50);

        AnchorPane opponentPane = opponentMap.getGameMapPane();
        opponentPane.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 200);
        opponentPane.setLayoutY(50);

        // underrubrik för spelplanerna
        Label playerLabel = new Label("Serverns spelplan");
        playerLabel.setLayoutX(150);
        playerLabel.setLayoutY(10);
        playerLabel.setTextFill(Color.RED);

        Label opponentLabel = new Label("Klientens spelplan");
        opponentLabel.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 280);
        opponentLabel.setLayoutY(10);
        opponentLabel.setTextFill(Color.GREEN);

        root.getChildren().addAll(playerPane, opponentPane, playerLabel, opponentLabel);

        Scene scene = new Scene(root, GameMap.GRID_SIZE * GameMap.CELL_SIZE * 2 + 250, GameMap.GRID_SIZE * GameMap.CELL_SIZE + 150);
        primaryStage.setTitle("Sänka Skepp");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}