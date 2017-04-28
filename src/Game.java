import Other.Cell;
import Other.UniverseType;

import java.util.ArrayList;

public class Game {
    private int universeW;
    private int universeH;
    private int speed;
    private boolean sleeping;
    private ArrayList<Cell[][]> gens;
    private Logic logic;
    private Thread th;

    public Game(UniverseType universeType, int universeWidth, int universeHeight) {
        this.universeW = universeWidth;
        this.universeH = universeHeight;
        logic = new Logic(universeType,universeWidth,universeHeight);
        speed = 1;
        sleeping = true;
    }
    public Game(UniverseType universeType, int universeW, int universeH, int frameWidth, int frameHeight) {
        logic = new Logic(universeType,universeW,universeH,frameWidth,frameHeight);
        gens = new ArrayList<>();
        speed = 1;
        sleeping = true;
    }

    public void firstGen(){
        gens = new ArrayList<>();
        gens.add(new Cell[universeH][universeW]);
    }

    public void nextGen(){
        int size = gens.size();
        gens.add(new Cell[universeH][universeW]);
        logic.nextGen(gens.get(size-1),gens.get(size));
    }

    public void setSpeed(int speed) {
        if(speed!=0) this.speed = speed;
    }

    public void start(){
        sleeping = false;
        if(th != null)
            (th = new Thread(()->{
                while (!sleeping){
                    outPut();
                    nextGen();
                    sleep(1000/speed);
                }
            })).start();
    }

    public void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop(){
        sleeping = true;
    }

    public void newGame(){
        firstGen();
        start();
    }

    public void outPut(){
        System.out.println("\n+++++++++++++++++++++++++++++++++++++++");
        int last = gens.size()-1;
        for(int i=0; i<logic.getFrame().getHeight(); i++){
            for(int j=0; j<logic.getFrame().getWidth(); j++)
                System.out.print((gens.get(last)[i][j].isALife()?1:0)+" ");
            System.out.println();
        }
    }
}
