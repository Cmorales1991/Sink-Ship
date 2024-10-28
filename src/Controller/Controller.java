package Controller;

import Model.Game;
import View.Scene;

public class Controller {

    private Game model;
    private Scene view;

    public Controller(Game model, Scene view) {
        this.model = model;
        this.view = view;

        // tar game (logik) view (gui) och skickar data mellan dessa
    }
}
