package Logic;

public class Parameters {
    public static UniverseType UNIVERSE_TYPE;
    public static int SPEED = 3;
    public static final int TILE_SIZE  = 50;
    public static final int FORM_WIDTH  = 800;
    public static final int FORM_HEIGHT = 600;
    public static final int VERT_SPACE  = 50;
    public static final int HORIZ_SPACE = 50;
    public static final int TILES_COUNT_W = (FORM_WIDTH -HORIZ_SPACE*2)/TILE_SIZE;
    public static final int TILES_COUNT_H = (FORM_HEIGHT-VERT_SPACE*2)/TILE_SIZE;
}
