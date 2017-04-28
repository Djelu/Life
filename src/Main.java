import Other.UniverseType;

import static Params.Params.universeH;
import static Params.Params.universeW;

public class Main {

    public static void main(String[] args) {

        universeH = 15;
        universeW = 15;
        Game game = new Game(new Universe(UniverseType.LIMITED,10,10));


    }
}
