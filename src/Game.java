import Logic.Logic;
import Logic.Cell;
import Logic.UniverseType;
import Logic.Gen;
import Graphic.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

import static Logic.Parameters.*;

public class Game extends Application{

    private boolean stoped;
    private boolean sleeping;
    private ArrayList<Gen> gens;
    private Logic logic;

    private Tile[][] grid;
    private Thread thread;
    private Task task;
    private Scene scene;

    private Button button;
    private Label labelTextGen;
    private Label labelNumGen;
    private Label labelTextSpeed;
    private Label labelNumSpeed;
    private CheckBox checkBoxLeft;
    private CheckBox checkBoxRight;
    private CheckBox checkBoxTop;
    private CheckBox checkBoxBot;
    private ScrollBar scrollBar;

    public static void main(String[] args) {
        launch(args);
    }

    private int ifHadSameGen(){
        int numSameGen = -1;
        int last = gens.size()-1;
        Gen lastGen = gens.get(last);
        Cell[][] lastCells = lastGen.getCells();
        int lastGenAliveCount = lastGen.getAliveCount();
        for (int o=last-1; o>=0; o--) {
            Gen gen = gens.get(o);
            if(gen.getAliveCount()==lastGenAliveCount){
                int aliveCount = 0;
                Cell[][] cells = gen.getCells();
                for(int i=0; i<cells.length; i++)
                    for(int j=0; j<cells[i].length; j++)
                        if(cells[i][j].isAlive() && lastCells[i][j].isAlive())
                            aliveCount++;
                if(aliveCount==lastGenAliveCount) {
                    numSameGen = o+1;
                    break;
                }
            }
        }
        return numSameGen;
    }

    public int nextGen(){
        int genCount;
        int aliveCount;
        int result;
        genCount = gens.size();
        if(stoped) {
            aliveCount = logic.firstGen(gens.get(genCount-1).getCells(), grid);
            result = genCount;
            gens.get(genCount-1).setAliveCount(aliveCount);
        }else {
            Gen gen = new Gen(new Cell[TILES_COUNT_H][TILES_COUNT_W]);
            if (genCount == 0) {//first gen
                aliveCount = logic.firstGen(gen.getCells(), grid);
            } else {
                aliveCount = logic.nextGen(gens.get(genCount - 1).getCells(), gen.getCells());
            }
            gen.setAliveCount(aliveCount);
            gens.add(gen);
            result = genCount+1;
        }
        return result;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        clearAll(TILES_COUNT_W,TILES_COUNT_H);
        scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Game of life");
        primaryStage.show();
    }

    private void clearAll(int width, int height){
        gens = new ArrayList<Gen>();
        logic = new Logic(width, height);
        sleeping = true;
        stoped = false;
        grid = new Tile[height][width];
    }

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(FORM_WIDTH, FORM_HEIGHT);

        for (int y = 0; y < TILES_COUNT_H; y++) {
            for (int x = 0; x < TILES_COUNT_W; x++) {
                Tile tile = new Tile(x, y, false);
                grid[y][x] = tile;
                root.getChildren().add(tile);
            }
        }

        button = new Button();
        button.setPrefSize(100,35);
        button.setText("Старт");
        button.setOnMouseClicked(event -> go());
        root.getChildren().addAll(button);


        labelTextGen = new Label();
        labelTextGen.setText("Поколение номер: ");
        labelNumGen = new Label();
        labelNumGen.setText("1");

        labelTextSpeed = new Label();
        labelTextSpeed.setText("Скорость смены поколений:");
        labelNumSpeed = new Label();
        labelNumSpeed.setText("3");

        root.getChildren().addAll(labelNumGen,labelTextGen,labelTextSpeed,labelNumSpeed);

        checkBoxTop = new CheckBox();
        checkBoxTop.setText("Замкнуть");
        checkBoxTop.setSelected(false);
        checkBoxTop.selectedProperty().addListener((ov, old_val, new_val) -> {
            checkBoxBot.setSelected(new_val);
            boolean closedByHorizontal = checkBoxLeft.isSelected()&&checkBoxRight.isSelected();
            UNIVERSE_TYPE = new_val
                    ?(closedByHorizontal ?UniverseType.CLOSED :UniverseType.CLOSED_BY_VERTICAL)
                    :(closedByHorizontal ?UniverseType.CLOSED_BY_HORIZONTAL :UniverseType.LIMITED);
        });


        checkBoxBot = new CheckBox();
        checkBoxBot.setText("Замкнуть");
        checkBoxBot.setSelected(false);
        checkBoxBot.selectedProperty().addListener((ov, old_val, new_val) -> {
            checkBoxTop.setSelected(new_val);
            boolean closedByHorizontal = checkBoxLeft.isSelected()&&checkBoxRight.isSelected();
            UNIVERSE_TYPE = new_val
                    ?(closedByHorizontal ?UniverseType.CLOSED :UniverseType.CLOSED_BY_VERTICAL)
                    :(closedByHorizontal ?UniverseType.CLOSED_BY_HORIZONTAL :UniverseType.LIMITED);
        });

        checkBoxLeft = new CheckBox();
        checkBoxLeft.setSelected(true);
        checkBoxLeft.selectedProperty().addListener((ov, old_val, new_val) -> {
            checkBoxRight.setSelected(new_val);
            boolean closedByVertical = checkBoxTop.isSelected()&&checkBoxBot.isSelected();
            UNIVERSE_TYPE = new_val
                    ?(closedByVertical ?UniverseType.CLOSED :UniverseType.CLOSED_BY_HORIZONTAL)
                    :(closedByVertical ?UniverseType.CLOSED_BY_VERTICAL :UniverseType.LIMITED);
        });

        checkBoxRight = new CheckBox();
        checkBoxRight.setSelected(true);
        checkBoxRight.selectedProperty().addListener((ov, old_val, new_val) -> {
            checkBoxLeft.setSelected(new_val);
            boolean closedByVertical = checkBoxTop.isSelected()&&checkBoxBot.isSelected();
            UNIVERSE_TYPE = new_val
                    ?(closedByVertical ?UniverseType.CLOSED :UniverseType.CLOSED_BY_HORIZONTAL)
                    :(closedByVertical ?UniverseType.CLOSED_BY_VERTICAL :UniverseType.LIMITED);
        });
        root.getChildren().addAll(checkBoxBot,checkBoxLeft,checkBoxRight,checkBoxTop);

        scrollBar = new ScrollBar();
        scrollBar.setPrefSize(FORM_WIDTH/4,button.getHeight()/2);
        scrollBar.setMin(1);
        scrollBar.setMax(100);
        scrollBar.setValue(10);
        scrollBar.valueProperty().addListener((ov, old_val, new_val) -> labelNumSpeed.setText(String.valueOf(SPEED=new_val.intValue())));
        root.getChildren().add(scrollBar);

        updateContentPosition();
        return root;
    }

    private void updateContentPosition(){
        button.setTranslateX(FORM_WIDTH-FORM_WIDTH/7);
        button.setTranslateY(FORM_HEIGHT-VERT_SPACE*3/4);

        labelTextGen.setTranslateX(HORIZ_SPACE);
        labelTextGen.setTranslateY(VERT_SPACE-20);

        labelNumGen.setTranslateX(HORIZ_SPACE+130);
        labelNumGen.setTranslateY(VERT_SPACE-20);

        labelTextSpeed.setTranslateX(HORIZ_SPACE);
        labelTextSpeed.setTranslateY(VERT_SPACE+TILES_COUNT_H*TILE_SIZE+5);

        labelNumSpeed.setTranslateX(HORIZ_SPACE+165);
        labelNumSpeed.setTranslateY(VERT_SPACE+TILES_COUNT_H*TILE_SIZE+5);

        checkBoxTop.setTranslateX(FORM_WIDTH/2);
        checkBoxTop.setTranslateY(VERT_SPACE/2-checkBoxTop.getHeight()/2);

        checkBoxBot.setTranslateX(FORM_WIDTH/2);
        checkBoxBot.setTranslateY(FORM_HEIGHT-VERT_SPACE/2-checkBoxBot.getHeight()/2);

        checkBoxLeft.setTranslateX(HORIZ_SPACE/2-checkBoxLeft.getWidth()/2);
        checkBoxLeft.setTranslateY(FORM_HEIGHT/2);

        checkBoxRight.setTranslateX(FORM_WIDTH-HORIZ_SPACE/2-checkBoxRight.getWidth()/2);
        checkBoxRight.setTranslateY(FORM_HEIGHT/2);

        scrollBar.setTranslateX(HORIZ_SPACE);
        scrollBar.setTranslateY(FORM_HEIGHT-VERT_SPACE+25);
    }

    public void stop(){
        sleeping = true;
        Platform.runLater(new Thread(()-> button.setText("Старт")));
    }

    public void go(){
        sleeping = !sleeping;

        if(!sleeping){
            if(thread!=null) {
                if(task!=null) {
                    task.cancel();
                    task=null;
                }
                thread.interrupt();
                thread = null;
                gens.clear();
            }
            Platform.runLater(new Thread(()-> button.setText("Стоп")));
            (thread = new Thread(()->{
                (task = new Task<Void>(){
                    @Override
                    protected Void call() throws Exception {
                        int numGen;
                        int numSameGen;
                        while (!isCancelled()) {
                            if (!sleeping) {
                                print(numGen = nextGen());
                                stoped = false;
                                if ((numSameGen = ifHadSameGen()) != -1) {
                                    stop();
                                    String titleText = "Конец игры";
                                    StringBuilder contentText = new StringBuilder();
                                    if (numGen - numSameGen == 1)
                                        contentText.append("Сложилась стабильная конфигурация.");
                                    else
                                        contentText.append("Сложилась переодическая конфигурация.");
                                    contentText.append("\nПоколение ").append(numSameGen).append(" повторилось в текущем(").append(numGen).append(").");

                                    Platform.runLater(new Thread(()->alertBuilder(titleText,contentText.toString())));
                                    break;
                                } else if (gens.get(numGen - 1).getAliveCount() == 0) {
                                    stop();
                                    Platform.runLater(new Thread(()->alertBuilder("Конец игры","Не осталось живых клеток.")));
                                    break;
                                }
                                Thread.sleep(1000 / SPEED);

                            }
                        }
                        return null;
                    }
                }).run();
            })).start();
        }else {
            if(thread!=null) {
                if(task!=null) {
                    task.cancel();
                    task=null;
                }
                thread.interrupt();
                thread = null;
                stoped = true;
            }
            Platform.runLater(new Thread(()-> button.setText("Старт")));
        }

    }

    private void alertBuilder(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void print(int numGen){
        int last = gens.size()-1;
        Cell[][] lastCells = gens.get(last).getCells();
        for(int i=0; i<lastCells.length; i++){
            for(int j=0; j<lastCells[i].length; j++)
                grid[i][j].setColored(lastCells[i][j].isAlive());
        }
        Platform.runLater(new Thread(()-> labelNumGen.setText(String.valueOf(numGen))));
    }
}
