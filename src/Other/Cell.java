package Other;

import java.util.Random;

public class Cell {
    private boolean isLife;

    public Cell() {
        this.isLife = false;
    }

    public Cell(boolean isLife) {
        this.isLife = isLife;
    }

    public boolean isLife() {
        return isLife;
    }

    public void setLife(boolean life) {
        isLife = life;
    }
}
