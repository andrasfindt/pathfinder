package xyz.andrasfindt.ai.ui.viewcontroller;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import xyz.andrasfindt.ai.Player;
import xyz.andrasfindt.ai.DotGame;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.ImageObstacle;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.Vector2D;

import java.util.List;

public class MazeGameController implements Listener {
    public static final Color DEFAULT = Color.BLUEVIOLET;

    @FXML
    public CheckBox solved;
    @FXML
    public Label popSize;
    @FXML
    public Label maxFitness;
    @FXML
    public Label stepsTaken;
    @FXML
    public Label speed;
    @FXML
    public Label generation;
    @FXML
    public Label mutationRate;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Canvas gameCanvasBackground;

    private GraphicsContext graphicsContext;
    private double canvasWidth;
    private double canvasHeight;
    private Color currentColor = Color.BLACK;
    private AnimationTimer animator;
    private DotGame dotGame;

    private boolean drawOldLocations = false;
    public static final double SCALE = Game.goal.distance(0, Game.SCREEN_HEIGHT);

    public void initialize() {
        graphicsContext = gameCanvas.getGraphicsContext2D();
        canvasWidth = graphicsContext.getCanvas().getWidth();
        canvasHeight = graphicsContext.getCanvas().getHeight();
        drawBackground();
        initDraw();

        solved.setSelected(false);
        stepsTaken.setText("1000");
        generation.setText("1");
        popSize.setText(String.valueOf(Game.POPULATION_SIZE));
        speed.setText(String.valueOf(Game.SPEED_LIMIT));
        maxFitness.setText("---");
        mutationRate.setText(String.format("%f%%", Game.MUTATION_RATE * 100d));

        dotGame = new DotGame(this);
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                if (!drawOldLocations) {
                    reset();
                }

                popSize.setText(String.valueOf(dotGame.getAliveCount()));
                dotGame.churn();
            }
        };
    }

    private void drawBackground() {
        List<ImageObstacle> imageObstacles = Game.getImageObstacles();
        GraphicsContext graphicsContext2D = gameCanvasBackground.getGraphicsContext2D();
        PixelWriter pixelWriter = graphicsContext2D.getPixelWriter();
        for (ImageObstacle imageObstacle : imageObstacles) {
            byte[][] image = imageObstacle.getImage();
            for (int x = 0; x < image.length; x++) {
                int lengthY = image[x].length;
                for (int y = 0; y < lengthY; y++) {
                    pixelWriter.setColor(x, y, image[x][y] <= Game.COLLISION_THRESHOLD ? Color.BLACK : Color.WHITE);
                }
            }
        }
        graphicsContext2D.setStroke(Color.RED);
        graphicsContext2D.setFill(Color.RED);
        graphicsContext2D.fillOval(Game.goal.getX() - 3d, Game.goal.getY() - 3d, 6d, 6d);
    }

    private void initDraw() {
        reset();
    }

    public void start(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).setVisible(false);
        animator.start();
    }

    @Override
    public void draw(Vector2D position, boolean isBest) {
        if (isBest) {
            currentColor = Color.LIGHTGREEN;
            graphicsContext.setStroke(currentColor);
            graphicsContext.setFill(currentColor);
        } else if (currentColor != DEFAULT) {
            //todo noo!
//            double d = position.distance(Game.goal);
//            System.out.println(d);
//            System.out.println(SCALE);
//            double scaled = d/ SCALE;
//            currentColor = Color.hsb(DEFAULT.getHue(), scaled, 1.0);//DEFAULT;
            currentColor = DEFAULT;
            graphicsContext.setStroke(currentColor);
            graphicsContext.setFill(currentColor);
        }
        graphicsContext.fillOval(position.getX() - 2d, position.getY() - 2d, 3d, 3d);
    }

    @Override
    public void updateStats(Player bestPlayer) {
        solved.setSelected(bestPlayer.hasReachedGoal());
        stepsTaken.setText(String.valueOf(bestPlayer.getStepCount()));
        generation.setText(String.valueOf(dotGame.getCurrentGeneration()));
//        popSize.setText(String.valueOf(Game.POPULATION_SIZE));
        popSize.setText(String.valueOf(dotGame.getAliveCount()));
        speed.setText(String.valueOf(Game.SPEED_LIMIT));
        maxFitness.setText(String.format("%f", bestPlayer.getFitness()));
        mutationRate.setText(String.format("%4.3f%%", Game.MUTATION_RATE * 100d));
        reset();
    }

    @Override
    public void reset() {
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        graphicsContext.setLineWidth(1);
        currentColor = DEFAULT;
        graphicsContext.setStroke(currentColor);
        graphicsContext.setFill(currentColor);

    }

    @FXML
    public void keyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getCharacter()) {
            case "+":
            case "=":
                dotGame.increaseSpeed();
                break;
            case "-":
            case "_":
                dotGame.decreaseSpeed();
                break;
        }
    }

    @FXML
    public void toggleDrawOld(ActionEvent actionEvent) {
        actionEvent.getEventType();
        drawOldLocations = !drawOldLocations;
    }
}
