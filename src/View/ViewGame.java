package View;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ViewGame extends View
{
    private GameMap serverMap, clientMap;

    private AnchorPane serverPane, clientPane;

    private Label serverLabel, clientLabel, statusLabel;

    public ViewGame(int height, int width)
    {
        super(height, width);

        init();
    }

    @Override
    protected void init()
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
        serverLabel = new Label("Serverns spelplan");
        serverLabel.setLayoutX(150);
        serverLabel.setLayoutY(10);
        serverLabel.setTextFill(Color.RED);

        clientLabel = new Label("Klientens spelplan");
        clientLabel.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 250);
        clientLabel.setLayoutY(10);
        clientLabel.setTextFill(Color.GREEN);

        statusLabel = new Label("Placera dina skepp!");
        statusLabel.setLayoutX(30);
        statusLabel.setLayoutY(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 110);

        pane.getChildren().addAll(serverPane, clientPane, serverLabel, clientLabel, statusLabel);
    }

    @Override
    public View update()
    {
        return this;
    }
}
