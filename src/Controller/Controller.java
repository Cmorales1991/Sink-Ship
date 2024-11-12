package Controller;

import Model.Attack;
import Model.ClientUser;
import Model.ServerUser;
import Model.User;
import View.ViewGame;

public class Controller {

    private User user;
    private ViewGame view;
    private boolean runGame = true;

    public Controller(User user, ViewGame view) {
        this.user = user;
        this.view = view;
    }

    private String reformatSentAttack(Attack attack) {

        String formattedAttack = String.valueOf(attack.getX());

        switch(attack.getY()) {
            case 0:
                formattedAttack += "a";
                break;
            case 1:
                formattedAttack += "b";
                break;
            case 2:
                formattedAttack += "c";
                break;
            case 3:
                formattedAttack += "d";
                break;
            case 4:
                formattedAttack += "e";
                break;
            case 5:
                formattedAttack += "f";
                break;
            case 6:
                formattedAttack += "g";
                break;
            case 7:
                formattedAttack += "h";
                break;
            case 8:
                formattedAttack += "i";
                break;
            case 9:
                formattedAttack += "j";
                break;
        }
        return formattedAttack;
    }

    private Attack reformatTakenAttack(String attack) {
        Attack reformattedAttack = new Attack(1,1);
        return reformattedAttack;
    }

    private String createString(String coordinate) {
        String exempelString = "m shot " + coordinate;
        return exempelString;
    }

    public void startGame() {

        if (user instanceof ServerUser) {

            ((ServerUser) user).initialize();

            while (runGame) {
                if (user.checkLost()) {
                    System.out.println("ServerPlayer lost!");
                    // skicka meddelande till klient att servern förlorat om checkLost() returnerar true
                    runGame = false;
                }
                else {
                    // user gör turn eller väntar på att motståndaren ska göra sin turn
                    // tar emot eller skickar data, uppdaterar map i User om beskjuten
                    // user.takeAttack() och user.performAttack() kallas här baserat på vad som händer
                    // skickas väl in i user.sendMessage() som argument om någon ska skickas iväg
                    // uppdaterar slutligen view så att ändringarna syns visuellt, med view.update(user.getMap)
                }
            }
        }
        if (user instanceof ClientUser) {

            ((ClientUser) user).initialize("localhost", 6667);

            //klienten startar spelet?
            ((ClientUser) user).sendMessage("i + attack");

            while (runGame) {
                if (user.checkLost()) {
                    System.out.println("ClientPlayer lost!");
                    // skicka meddelande till servern att klienten förlorat om checkLost() returnerar true
                    runGame = false;
                }
                else {
                    // (samma som däruppe)
                }
            }
        }
    }
}