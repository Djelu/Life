package View;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static sun.dc.pr.Rasterizer.TILE_SIZE;

public class Tile extends StackPane{
    private int x, y;
    private boolean colored;

    private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);

    public Tile(int x, int y, boolean colored) {
        this.x = x;
        this.y = y;
        this.colored = colored;

        border.setFill(Color.LIGHTGRAY);
        getChildren().addAll(border);

        setTranslateX(x * TILE_SIZE);
        setTranslateY(y * TILE_SIZE);

        setOnMouseClicked(e->click());
    }

    private void click(){
        colored =!colored;
        updateColor();
    }

    public boolean isColored() {
        return colored;
    }

    private void updateColor(){
        border.setFill(colored ?Color.BLACK :Color.LIGHTGRAY);
    }

    public void setColored(boolean colored) {
        this.colored = colored;
        updateColor();
    }
}
