package Logic;

public class Frame{
    private int width;
    private int height;

    private int defX;
    private int defY;

    public int x;
    public int y;

    public Frame(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.defX = (this.x = x);
        this.defY = (this.y = y);
    }

    public int getDefX() {
        return defX;
    }

    public int getDefY() {
        return defY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
