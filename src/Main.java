import Logic.UniverseType;

//import static Params.Params.universeH;
//import static Params.Params.universeW;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Game game = new Game(UniverseType.CLOSED,20,20,20,20);
        game.newGame();
    }
}
