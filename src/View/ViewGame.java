package View;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ViewGame extends View
{
    private GameMap serverMap, clientMap;
    private AnchorPane serverPane, clientPane;
    private Label serverLabel, clientLabel, statusLabel;

    public ViewGame(int height, int width, boolean isServer) {
        super(height, width);
        init(isServer);
    }

    @Override
    protected void init(boolean isServer)
    {
        // spelbräda för server o klient
        serverMap = new GameMap(true);
        clientMap = new GameMap(false);

        // positionering spelplan för serverplan och client
        serverPane = serverMap.getGameMapPane();
        serverPane.setLayoutX(50);
        serverPane.setLayoutY(50);

        clientPane = clientMap.getGameMapPane();
        clientPane.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 150);
        clientPane.setLayoutY(50);

        // underrubrik för spelplanerna
        serverLabel = new Label("SERVER'S SHIPS");
        serverLabel.setLayoutX(150);
        serverLabel.setLayoutY(10);
        serverLabel.setTextFill(Color.RED);

        clientLabel = new Label("CLIENT'S SHIPS");
        clientLabel.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 250);
        clientLabel.setLayoutY(10);
        clientLabel.setTextFill(Color.GREEN);

        pane.getChildren().addAll(serverPane, clientPane, serverLabel, clientLabel);

    }

    public void placeShips(int x, int y, boolean ifServerInstance) {
        Platform.runLater(() -> {
            if (ifServerInstance) {
                serverMap.updateMap(x, y, "p");
            } else {
                clientMap.updateMap(x, y, "p");
            }
        });
    }

    public void updateMaps(int x, int y, String status, boolean ifServerMap) {
        Platform.runLater(() -> {
            if (ifServerMap) {
                serverMap.updateMap(x, y, status);
            } else {
                clientMap.updateMap(x, y, status);
            }
        });
    }

    @Override
    public View update()
    {
        return this;
    }

    public AnchorPane getPane() {
        return pane;
    }
}
