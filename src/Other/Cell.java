package Other;

import java.util.Random;

public class Cell {
    private boolean isALife;

    public Cell() {
        this.isALife = new Random().nextBoolean();
    }

    public Cell(boolean isALife) {
        this.isALife = isALife;
    }

    public boolean isALife() {
        return isALife;
    }

    public void setALife(boolean ALife) {
        isALife = ALife;
    }
}
