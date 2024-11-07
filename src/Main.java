import Model.Attack;
import Model.ClientUser;
import Model.ServerUser;
import Model.UserMap;
import View.ViewGame;
import View.ViewMenu;
import View.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {

        // PRINTAR EN MASSA SAKER HÄR FÖR ATT VISA VAD JAG GJORT XD
        // det ska bort sen (kanske? vi kan ju ha kvar det om vi vill för att se bättre vad som händer)

        // skapa en UserMap, vilket är kartan som tillhör denna instans av programmet
        // jag tog bort EnemyMap-klassen, vi kommer inte att behöva den

        /*UserMap map = new UserMap();
        ServerUser server = new ServerUser(map);
        ClientUser client = new ClientUser(map);

        server.initialize();
        client.initialize("localhost", 6667);*/

        // viktigt: detta är inte hur programmet ska startas, det ska egentligen startas genom en Game-instans
        // visar bara lite nu hur jag tänkt med logiken

        // i detta exempel är det en serveruser, men både serveruser + clientuser extends user
        // så de har samma funktionalitet. det enda som ska implementeras i ServerUser + ClientUser
        // är ju egentligen olika sockets

        // printa kartan för att visa hur en usermap ser ut:

        //map.printMap();
        // det är alltså koordinater och en boolean som berättar om det är en båt där elr ej :)
        // tänker att game-klassen kan konvertera koordinaterna rätt, alltså att x blir bokstäver ist
        // för det ska ju vara A,0 ist för 0,0
        // men det är enklare att utföra logik på nummer så det blir ett sista steg

        // den här metoden returnerar en random Attack
        // en attack innehåller en koordinat (som inte attackerats tidigare)
        // attacker ska skickas fram och tillbaka mellan server/client :)

        // testar attackera koordinat 5,5 på vår egen karta (lol)
        //Attack attack = new Attack(5,5);
        //server.takeAttack(attack.getX(), attack.getY());

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        ViewGame view = new ViewGame(450, 900);

        primaryStage = view.getStage();

        primaryStage.show();
    }
}