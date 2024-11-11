package Controller;

import Model.Game;
import View.ViewGame;

public class Controller {

    private Game model;
    private ViewGame view;

    public Controller(Game model, ViewGame view) {
        this.model = model;
        this.view = view;
    }
}
