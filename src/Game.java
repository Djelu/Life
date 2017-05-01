import Logic.Logic;
import Logic.Cell;
import Logic.UniverseType;
import Logic.Gen;

import java.util.ArrayList;

public class Game {
    private int universeW;
    private int universeH;
    private int speed;
    private boolean sleeping;
    private ArrayList<Gen> gens;
    private Logic logic;
    private Thread th;

    public Game(UniverseType universeType, int universeWidth, int universeHeight) {
        this.universeW = universeWidth;
        this.universeH = universeHeight;
        gens = new ArrayList<Gen>();
        logic = new Logic(universeType,universeWidth,universeHeight);
        speed = 3;
        sleeping = true;
    }

    private int ifHadSameGen(){
        int numSameGen = -1;
        int last = gens.size()-1;
        Gen lastGen = gens.get(last);
        Cell[][] lastCells = lastGen.getCells();
        int lastGenAliveCount = lastGen.getAliveCount();
        for (int o=last-1; o>=0; o--) {
            Gen gen = gens.get(o);
            if(gen.getAliveCount()==lastGenAliveCount){
                int aliveCount = 0;
                Cell[][] cells = gen.getCells();
                for(int i=0; i<cells.length; i++)
                    for(int j=0; j<cells[i].length; j++)
                        if(cells[i][j].isAlive() && lastCells[i][j].isAlive())
                            aliveCount++;
                if(aliveCount==lastGenAliveCount) {
                    numSameGen = o+1;
                    break;
                }
            }
        }
        return numSameGen;
    }

    public int nextGen(){
        int genCount;
        int aliveCount;
        Gen gen = new Gen(new Cell[universeH][universeW]);
        if((genCount = gens.size())==0){//first gen

            aliveCount = logic.firstGen(gen.getCells());
        }else {
            aliveCount = logic.nextGen(gens.get(genCount - 1).getCells(), gen.getCells());
        }
        gen.setAliveCount(aliveCount);
        gens.add(gen);
        return genCount+1;
    }

    public void setSpeed(int speed) {
        if(speed!=0) this.speed = speed;
    }

    public void stop(){
        sleeping = true;
    }

    public void newGame(){
        if(!sleeping) stop();
        start();
    }

    public void start(){
        sleeping = false;
        if(th == null)
            (th = new Thread(()->{
                int numGen;
                int numSameGen;
                while (true){
                    if(!sleeping){
                        print(numGen = nextGen());
                        if((numSameGen=ifHadSameGen())!=-1){
                            stop();
                            System.out.print("\n\nКонец игры. ");
                            if(numGen-numSameGen==1)
                                System.out.println("Сложилась стабильная конфигурация.");
                            else
                                System.out.println("Сложилась переодическая конфигурация.");
                            System.out.println("Поколение "+numSameGen+" повторилось в текущем("+numGen+").");
                            break;
                        }else
                            if(gens.get(numGen-1).getAliveCount()==0){
                                System.out.println("\n\nКонец игры. Не осталось живых клеток.");
                                break;
                            }
                        sleep(1000/speed);
                    }
                }
            })).start();
    }

    public void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void print(int numGen){
        System.out.println("\n+++++++++++++++++++| "+numGen+" |++++++++++++++++++++");
        int last = gens.size()-1;
        Cell[][] lastCells = gens.get(last).getCells();
        for(int i=0; i<lastCells.length; i++){
            for(int j=0; j<lastCells[i].length; j++)
                System.out.print((lastCells[i][j].isAlive()?"Ш":" "));
            System.out.println();
        }
    }
}
