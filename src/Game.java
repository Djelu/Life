import Logic.Logic;
import Logic.Cell;
import Logic.UniverseType;
import Logic.Gen;
import View.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

import static Logic.Parameters.*;

public class Game extends Application{
//    private int universeW;
//    private int universeH;
    private boolean stoped;
    private boolean sleeping;
    private ArrayList<Gen> gens;
    private Logic logic;

    private Tile[][] grid;
    private Scene scene;
    private Button button;
    private Thread thread;
    private Task task;
//    public Game(UniverseType universeType, int universeWidth, int universeHeight, int speed) {
//        this.universeW = universeWidth;
//        this.universeH = universeHeight;
//        this.speed = speed;
//    }

    public static void main(String[] args) {
//        //Величина вселенной (в клетках)
//        int width = 20;
//        int height= 20;
//
//        //скорость жизни (1000мс/speed)
//        int speed = 10;
//
//        UniverseType universeType = UniverseType.CLOSED;              //замкнутая(неограниченная) со всех сторон
//        //UniverseType.LIMITED;             //ограниченная со всех сторон
//        //UniverseType.CLOSED_BY_VERTICAL;  //замкнутая по вертикали
//        //UniverseType.CLOSED_BY_HORIZONTAL;//замкнутая по горизонтали
//
//        new Game(universeType,width,height,speed);
        launch(args);
        //        game.newGame();
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
            Gen gen = new Gen(new Cell[TILES_HEIGHT][TILES_WIDTH]);
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
        gens = new ArrayList<Gen>();
        logic = new Logic(UniverseType.CLOSED, TILES_WIDTH, TILES_HEIGHT);
        sleeping = true;
        stoped = false;

        grid = new Tile[TILES_HEIGHT][TILES_WIDTH];

        scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(FORM_WIDTH, FORM_HEIGHT);

        for (int y = 0; y < TILES_HEIGHT; y++) {
            for (int x = 0; x < TILES_WIDTH; x++) {
                Tile tile = new Tile(x, y, false);
                grid[y][x] = tile;
                root.getChildren().add(tile);
            }
        }

        button = new Button();
        button.setTranslateX(FORM_WIDTH/2);
        button.setTranslateY(FORM_HEIGHT-FORM_HEIGHT/10);
        button.setPrefSize(100,50);
        button.setOnMouseClicked(event -> newGame());
        root.getChildren().add(button);

        return root;
    }

    public void stop(){
        sleeping = true;
    }

    public void newGame(){
//        if(!sleeping)
//            stop();
        go();
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
        }

    }

    private void alertBuilder(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
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
        Platform.runLater(new Thread(()->button.setText(String.valueOf(numGen))));
    }
}
