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
    public Game(UniverseType universeType, int universeWidth, int universeHeight, int frameWidth, int frameHeight) {
        this.universeW = universeWidth;
        this.universeH = universeHeight;
        logic = new Logic(universeType,universeW,universeH,frameWidth,frameHeight);
        speed = 1;
        sleeping = true;
    }

    int[][] testGen = new int[][]{{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,
            0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,
            0,0,0,0,0,1,0,1,1,1,1,1,1,0,1,0,0,0,0,0,
            0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,
            0,0,0,0,0,1,0,1,1,1,1,1,1,0,1,0,0,0,0,0,
            0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,
            0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    }};

    public void firstGen(){
        gens = new ArrayList<>();
        Cell[][] gen = new Cell[universeH][universeW];
//        logic.firstGen(gen);
        intToCell(testGen,gen);
        gens.add(gen);
    }

    private void intToCell(int[][] testGen, Cell[][] gen){
        for(int i=0; i<testGen.length; i++)
            for(int j=0; j<testGen[0].length; j++)
                gen[i][j].setLife(testGen[i][j]==1);
    }

    public void nextGen(){
        Cell[][] gen = new Cell[universeH][universeW];
        logic.nextGen(gens.get(gens.size()-1),gen);
        gens.add(gen);
    }

    public void setSpeed(int speed) {
        if(speed!=0) this.speed = speed;
    }

    public void start(){
        sleeping = false;
        if(th == null)
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
        if(!sleeping) stop();
        firstGen();
        start();
    }

    public void outPut(){
        System.out.println("\n+++++++++++++++++++++++++++++++++++++++");
        int last = gens.size()-1;
        for(int i=0; i<logic.getFrame().getHeight(); i++){
            for(int j=0; j<logic.getFrame().getWidth(); j++)
                System.out.print((gens.get(last)[i][j].isLife()?1:0)+" ");
            System.out.println();
        }
    }
}
