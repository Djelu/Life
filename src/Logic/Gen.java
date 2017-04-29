package Logic;

public class Gen {
    private Cell[][] cells;
    private int aliveCount;

    public Gen(Cell[][] cells) {
        this.cells = cells;
        this.aliveCount = 0;
    }

    public void intToCell(int[][] intCells){
        boolean alive;
        for(int i=0; i<intCells.length; i++)
            for(int j=0; j<intCells[i].length; j++) {
                cells[i][j] = new Cell(alive = intCells[i][j] == 1);
                if(alive)
                    aliveCount++;
            }
    }
    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public int getAliveCount() {
        return aliveCount;
    }

    public void setAliveCount(int aliveCount) {
        this.aliveCount = aliveCount;
    }
}
