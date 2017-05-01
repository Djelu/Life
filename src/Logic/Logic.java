package Logic;

import Graphic.Tile;

import static Logic.Parameters.UNIVERSE_TYPE;
import static Logic.UniverseType.CLOSED;
import static Logic.UniverseType.CLOSED_BY_HORIZONTAL;
import static Logic.UniverseType.CLOSED_BY_VERTICAL;

public class Logic {
    private int universeW;
    private int universeH;

    public Logic(int universeW, int universeH) {
        this.universeW = universeW;
        this.universeH = universeH;
    }

    public int neighborsCount(int x, int y, Cell[][] gen){
        int count = 0;
        Point point;
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
        switch (UNIVERSE_TYPE){
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
        return !((((x < 0)||(x >= universeW))&&((UNIVERSE_TYPE!= CLOSED_BY_HORIZONTAL && UNIVERSE_TYPE!= CLOSED))) ||
                 (((y < 0)||(y >= universeH))&&((UNIVERSE_TYPE!= CLOSED_BY_VERTICAL   && UNIVERSE_TYPE!= CLOSED))));
    }

    public int firstGen(Cell[][] newGenCells, Tile[][] grid){
//        Random random = new Random();
//        int aliveCount = 0;
//        boolean alive;
//        for(int i=0; i<newGenCells.length; i++)
//            for(int j=0; j<newGenCells[i].length; j++){
//                newGenCells[i][j] = new Cell(alive = random.nextBoolean());
//                if(alive)
//                    aliveCount++;
//        }
        int aliveCount = 0;
        boolean alive;
        for(int i=0; i<newGenCells.length; i++)
            for(int j=0; j<newGenCells[i].length; j++){
                newGenCells[i][j] = new Cell(alive=grid[i][j].isColored());
                if(alive)
                    aliveCount++;
            }
        return aliveCount;
    }

    public int nextGen(Cell[][] lastGenCells, Cell[][] newGenCells){
        int aliveCount = 0;
        int nCount;
        for(int i=0; i<newGenCells.length; i++)
            for(int j=0; j<newGenCells[i].length; j++){
                newGenCells[i][j] = new Cell();
                nCount = neighborsCount(j,i,lastGenCells);
                boolean alive;
                if(lastGenCells[i][j].isAlive()){
                    if(alive = (nCount==2||nCount==3)) aliveCount++;
                }else
                    if(alive = nCount==3) aliveCount++;
                newGenCells[i][j].setAlive(alive);
            }
        return aliveCount;
    }
}
