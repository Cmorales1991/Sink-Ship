package Controller;

import Model.Game;
import View.Scenes;

public class Controller {

    private Game model;
    private Scenes view;

    public Controller(Game model, Scenes view) {
        this.model = model;
        this.view = view;

        // tar game (logik) view (gui) och skickar data mellan dessa
    }
}
