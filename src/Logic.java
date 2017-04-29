import Other.Cell;
import Other.Frame;
import Other.Point;
import Other.UniverseType;
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
//        if (withSelf && logic[x][y].isLife()) count++;
        Point point;
        normalize(point = new Point(x,y)); if (inUniverse(point.x-1,point.y-1) && gen[point.x-1][point.y-1].isLife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(   point.x  ,point.y-1) && gen[point.x  ][point.y-1].isLife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point.x+1,point.y-1) && gen[point.x+1][point.y-1].isLife()) count++;

        normalize(point = new Point(x,y)); if (inUniverse(point.x-1,point.y-1) && gen[point.x-1][point.y  ].isLife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point.x+1,point.y-1) && gen[point.x+1][point.y  ].isLife()) count++;

        normalize(point = new Point(x,y)); if (inUniverse(point.x-1,point.y+1) && gen[point.x-1][point.y+1].isLife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(   point.x  ,point.y+1) && gen[point.x  ][point.y+1].isLife()) count++;
        normalize(point = new Point(x,y)); if (inUniverse(point.x+1,point.y+1) && gen[point.x+1][point.y+1].isLife()) count++;
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
                gen[i][j] = new Cell();
                nCount = neighborsCount(j,i,lastGen);
                if(lastGen[i][j].isLife()){
                    if(nCount<2||nCount>3) gen[i][j].setLife(false);
                }else {
                    if(nCount==3         ) gen[i][j].setLife(true );
                }
            }
    }

    public Frame getFrame() {
        return frame;
    }
}
