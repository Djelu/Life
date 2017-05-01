import Logic.UniverseType;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        //Величина вселенной (в клетках)
        int width = 20;
        int height= 20;

        //скорость жизни (1000мс/speed)
        int speed = 10;

        UniverseType universeType = UniverseType.CLOSED;              //замкнутая(неограниченная) со всех сторон
                                    //UniverseType.LIMITED;             //ограниченная со всех сторон
                                    //UniverseType.CLOSED_BY_VERTICAL;  //замкнутая по вертикали
                                    //UniverseType.CLOSED_BY_HORIZONTAL;//замкнутая по горизонтали
        Game game = new Game(universeType,width,height,speed);
        game.newGame();
    }
}
