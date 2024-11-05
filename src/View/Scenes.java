package View;

import Model.Ship;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Scenes extends Application {
    private static final double WINDOWSIZE = 450;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        //windows ruta
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(WINDOWSIZE, WINDOWSIZE);

        // spelbräda för server o klient
        GameMap serverMap = new GameMap(true);
        GameMap clientMap = new GameMap(false);

        // positionering spelplan för serverplan och client
        AnchorPane serverPane = serverMap.getGameMapPane();
        serverPane.setLayoutX(50);
        serverPane.setLayoutY(50);

        AnchorPane clientPane = clientMap.getGameMapPane();
        clientPane.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 150);
        clientPane.setLayoutY(50);

        // underrubrik för spelplanerna
        Label serverLabel = new Label("Serverns spelplan");
        serverLabel.setLayoutX(150);
        serverLabel.setLayoutY(10);
        serverLabel.setTextFill(Color.RED);

        Label clientLabel = new Label("Klientens spelplan");
        clientLabel.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 250);
        clientLabel.setLayoutY(10);
        clientLabel.setTextFill(Color.GREEN);

        statusLabel = new Label("Placera dina skepp!");
        statusLabel.setLayoutX(30);
        statusLabel.setLayoutY(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 110);

        anchorPane.getChildren().addAll(serverPane, clientPane, serverLabel, clientLabel, statusLabel);
        Scene scene = new Scene(anchorPane);
        primaryStage.setTitle("Sänka Skepp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}