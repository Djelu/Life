package Logic;

public class Parameters {
    public static UniverseType UNIVERSE_TYPE = UniverseType.CLOSED_BY_HORIZONTAL;
    public static int SPEED = 3;
    public static int TILE_SIZE  = 50;
    public static int FORM_WIDTH  = 800;
    public static int FORM_HEIGHT = 600;
    public static int VERT_SPACE  = 50;
    public static int HORIZ_SPACE = 50;
    public static int TILES_COUNT_W = (FORM_WIDTH -HORIZ_SPACE*2)/TILE_SIZE;
    public static int TILES_COUNT_H = (FORM_HEIGHT-VERT_SPACE*2)/TILE_SIZE;
}
