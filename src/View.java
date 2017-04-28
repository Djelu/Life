import Other.Cell;
import Other.Frame;
import Other.Point;
import Other.UniverseType;

import java.util.Random;

public class View {
    private UniverseType universeType;
//    private Cell[][] gen;
    private int universeW;
    private int universeH;
    private Frame frame;

    public View(UniverseType universeType, int universeWidth, int universeHeight) {
        this.universeW = universeWidth;
        this.universeH = universeHeight;
        int frameX = 0;
        int frameY = 0;
        this.frame = new Frame(universeWidth,universeHeight,frameX,frameY);
        this.universeType = universeType;
    }
    public View(UniverseType universeType, int universeWidth, int universeHeight, int frameWidth, int frameHeight) {
        this.universeW = universeWidth;
        this.universeH = universeHeight;
        int frameX = (universeWidth -frameWidth )/2;
        int frameY = (universeHeight-frameHeight)/2;
        this.frame = new Frame(universeWidth,universeHeight,frameX,frameY);
        this.universeType = universeType;
    }

    public View(UniverseType universeType, Cell[][] gen) {
        int universeHeight = gen.length;
        int universeWidth  = gen[0].length;
        this.universeW = universeWidth;
        this.universeH = universeHeight;
        int frameX = 0;
        int frameY = 0;
        this.frame = new Frame(universeWidth,universeHeight,frameX,frameY);
        this.universeType = universeType;
    }
    public View(UniverseType universeType, Cell[][] gen, int frameWidth, int frameHeight) {
        int universeHeight = gen.length;
        int universeWidth  = gen[0].length;
        this.universeW = universeWidth;
        this.universeH = universeHeight;
        int frameX = (universeWidth -frameWidth )/2;
        int frameY = (universeHeight-frameHeight)/2;
        this.frame = new Frame(universeWidth,universeHeight,frameX,frameY);
        this.universeType = universeType;
    }

    public int neighborsCount(int x, int y, Cell[][] gen){
        int count = 0;
//        if (withSelf && universe[x][y].isALife()) count++;
        Point point;
        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x-1][point.y-1].isALife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x  ][point.y-1].isALife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x+1][point.y-1].isALife()) count++;

        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x-1][point.y  ].isALife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x+1][point.y  ].isALife()) count++;

        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x-1][point.y+1].isALife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x  ][point.y+1].isALife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point) && gen[point.x+1][point.y+1].isALife()) count++;
        return count;
    }

    private void normalize(Point point){
        switch (universeType){
            case CLOSED:{
                if(point.x<0) point.x = universeW-point.x; else if(point.x>=universeW)point.x = point.x-universeW;
                if(point.y<0) point.y = universeH-point.y; else if(point.y>=universeH)point.y = point.y-universeH;
            }break;
            case CLOSED_BY_HORIZONTAL:{
                if(point.x<0) point.x = universeW-point.x; else if(point.x>=universeW)point.x = point.x-universeW;
            }break;
            case CLOSED_BY_VERTICAL:{
                if(point.y<0) point.y = universeH-point.y; else if(point.y>=universeH)point.y = point.y-universeH;
            }break;
            default:{
                if(point.x<0) point.x = 0; else if(point.x>=universeW)point.x = universeW-1;
                if(point.y<0) point.y = 0; else if(point.y>=universeH)point.y = universeH-1;
            }break;
        }
    }

    private boolean inUniverse(Point point){
        return !((((point.x < 0)||(point.x >= universeW))&&((universeType!=UniverseType.CLOSED_BY_HORIZONTAL && universeType!= UniverseType.CLOSED))) ||
                 (((point.y < 0)||(point.y >= universeH))&&((universeType!=UniverseType.CLOSED_BY_VERTICAL   && universeType!=UniverseType.CLOSED))));
    }

    public void moveFrame(int mx, int my){
        frame.x+=mx;
        frame.y+=my;
//        if(Math.abs(frame.x)==universeW) frame.x = frame.getDefX();
//        if(Math.abs(frame.y)==universeH) frame.y = frame.getDefY();
    }

    public void firstGen(Cell[][] gen){
        Random random = new Random();
        for(int i=frame.y; i<frame.getHeight(); i++)
            for(int j=frame.x; j<frame.getWidth(); j++)
                gen[i][j] = new Cell(random.nextBoolean());
    }

    public void nextGen(Cell[][] lastGen, Cell[][] gen){
        int nCount;
        for(int i=frame.y; i<frame.getHeight(); i++)
            for(int j=frame.x; j<frame.getWidth(); j++){
                nCount = neighborsCount(j,i,lastGen);
                if(lastGen[i][j].isALife()){
                    if(nCount<2||nCount>3) gen[i][j].setALife(false);
                }else {
                    if(nCount==3         ) gen[i][j].setALife(true );
                }
            }
    }
}
