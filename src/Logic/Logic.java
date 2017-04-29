package Logic;

import java.util.Random;

//import static Params.Params.universeW;
//import static Params.Params.universeH;

public class Logic {
    private int universeW;
    private int universeH;
    private UniverseType universeType;
    private Frame frame;

    public Logic(UniverseType universeType, int universeW, int universeH) {
        this.universeType = universeType;
        this.universeW = universeW;
        this.universeH = universeH;
        int frameX = 0;
        int frameY = 0;
        this.frame = new Frame(universeW,universeH,frameX,frameY);
    }
    public Logic(UniverseType universeType, int universeW, int universeH, int frameWidth, int frameHeight) {
        this.universeType = universeType;
        this.universeW = universeW;
        this.universeH = universeH;
        int frameX = (universeW-frameWidth )/2;
        int frameY = (universeH-frameHeight)/2;
        this.frame = new Frame(universeW,universeH,frameX,frameY);
    }

    public int neighborsCount(int x, int y, Cell[][] gen){
        int count = 0;
        Point point;
        //normalize(point = new Point(x,y));
        normalize(point = new Point(x-1,y-1)); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;
        normalize(point = new Point(   x  ,y-1)); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;
        normalize(point = new Point(x+1,y-1)); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;

        normalize(point = new Point(x-1,   y  )); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;
        normalize(point = new Point(x+1,   y  )); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;

        normalize(point = new Point(x-1,y+1)); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;
        normalize(point = new Point(   x  ,y+1)); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;
        normalize(point = new Point(x+1,y+1)); if (inUniverse(point.x,point.y) && gen[point.y][point.x].isAlive()) count++;
        return count;
    }

    private void normalize(Point point){
        switch (universeType){
            case CLOSED:{
                if(point.x<0) point.x = universeW+point.x; else if(point.x>=universeW)point.x = point.x-universeW;
                if(point.y<0) point.y = universeH+point.y; else if(point.y>=universeH)point.y = point.y-universeH;
            }break;
            case CLOSED_BY_HORIZONTAL:{
                if(point.x<0) point.x = universeW+point.x; else if(point.x>=universeW)point.x = point.x-universeW;
            }break;
            case CLOSED_BY_VERTICAL:{
                if(point.y<0) point.y = universeH+point.y; else if(point.y>=universeH)point.y = point.y-universeH;
            }break;
            default:{
                if(point.x<0) point.x = 0; else if(point.x>=universeW)point.x = universeW-1;
                if(point.y<0) point.y = 0; else if(point.y>=universeH)point.y = universeH-1;
            }break;
        }
    }

    private boolean inUniverse(int x, int y){
        return !((((x < 0)||(x >= universeW))&&((universeType!=UniverseType.CLOSED_BY_HORIZONTAL && universeType!= UniverseType.CLOSED))) ||
                 (((y < 0)||(y >= universeH))&&((universeType!=UniverseType.CLOSED_BY_VERTICAL   && universeType!=UniverseType.CLOSED))));
    }

    public void moveFrame(int mx, int my){
        frame.x+=mx;
        frame.y+=my;
//        if(Math.abs(frame.x)==universeW) frame.x = frame.getDefX();
//        if(Math.abs(frame.y)==universeH) frame.y = frame.getDefY();
    }

    public int firstGen(Cell[][] cells){
        int aliveCount = 0;
        boolean alive;
        Random random = new Random();
        for(int i=frame.y; i<frame.getHeight(); i++)
            for(int j=frame.x; j<frame.getWidth(); j++){
                cells[i][j] = new Cell(alive = random.nextBoolean());
                if(alive) aliveCount++;
        }
        return aliveCount;
    }

    public int nextGen(Cell[][] lastGenCells, Cell[][] cells){
        int aliveCount = 0;
        int nCount;
        for(int i=0; i<cells.length; i++)
            for(int j=0; j<cells[i].length; j++){
                cells[i][j] = new Cell();
                nCount = neighborsCount(j,i,lastGenCells);
                boolean alive;
                if(lastGenCells[i][j].isAlive()){
                    if(alive = (nCount==2||nCount==3)) aliveCount++;
                }else
                    if(alive = nCount==3) aliveCount++;
                cells[i][j].setAlive(alive);
            }
        return aliveCount;
    }


    public Frame getFrame() {
        return frame;
    }
}
