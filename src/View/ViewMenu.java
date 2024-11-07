package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ViewMenu extends View
{

    View nextView;

    public ViewMenu(int height, int width)
    {
        super(height, width);

        nextView = null;

        //init kallas för att sätta upp det grafiska så som knappar, labels osv.
        init();
    }


    public void input()
    {

        //kolla efter musklick

        //1. om musklick på knapp server
        //2. kalla på controller?

    }

    @Override
    protected void init()
    {
        Button startButton = new Button("Start");
        startButton.setPrefSize(100, 100);
        startButton.setLayoutY((height / 2) - (startButton.getLayoutBounds().getHeight() / 2));
        startButton.setLayoutX((width / 2) - (startButton.getLayoutBounds().getWidth() / 2));


        EventHandler<ActionEvent> startGameEvent = new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                nextView = new ViewGame(height, width);
            }
        };

        pane.getChildren().add(startButton);
    }

    @Override
    public View update()
    {
        return nextView;
    }
}
