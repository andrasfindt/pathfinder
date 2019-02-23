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
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import xyz.andrasfindt.ai.DotGame;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.Status;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.ImageObstacle;
import xyz.andrasfindt.ai.ui.drawing.DrawingEvent;
import xyz.andrasfindt.ai.ui.drawing.DrawingHandler;
import xyz.andrasfindt.ai.ui.drawing.DrawingListener;
import xyz.andrasfindt.ai.ui.drawing.DrawingViewWrapper;
import xyz.andrasfindt.ai.ui.drawing.ImageUtil;

import java.util.List;

public class MazeGameController implements Listener, DrawingListener {
    private static final double GREEN_HUE = Color.GREEN.getHue();
    private static final double GREEN_HUE_SCALING_FACTOR = GREEN_HUE / (Game.Setup.SCREEN_HEIGHT - Game.Setup.goal.y / 2d - 1d);
    private static final Color OBSTACLE_COLOR = Color.FIREBRICK;
    private static final String LOG_MESSAGE_FORMAT = "%s %s gen: %d max: %s step(t/m): %d/%d max_speed: %s creeps(a/p): %d/%d m-rate: %s r-seed: %d\n";
    private static final Color DEFAULT_COLOR = Color.hsb(0d, 1d, 1d);//Color.BLUEVIOLET;
    //    private static final double HUE_SCALE = ;
    //    private static final double SATURATION_SCALE = 1d / (Game.Setup.SCREEN_HEIGHT - 1);
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
    private GraphicsContext backgroundGraphicsContext;
    private double canvasWidth;
    private double canvasHeight;
    private Color currentColor = Color.BLACK;
    private AnimationTimer animator;
    private DotGame dotGame;
    private boolean drawOldLocations = false;

    public void initialize() {
        graphicsContext = gameCanvas.getGraphicsContext2D();
        backgroundGraphicsContext = gameCanvasBackground.getGraphicsContext2D();
        canvasWidth = graphicsContext.getCanvas().getWidth();
        canvasHeight = graphicsContext.getCanvas().getHeight();
        drawBackground();
        initDraw();

        solved.setSelected(false);
        stepsTaken.setText("1000");
        generation.setText("1");
        popSize.setText(String.valueOf(Game.Setup.POPULATION_SIZE));
        speed.setText(String.valueOf(Game.Setup.SPEED_LIMIT));
        maxFitness.setText("---");
        mutationRate.setText(String.format("%f%%", Game.Setup.MUTATION_RATE * 100d));

        dotGame = new DotGame(this);
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                if (!drawOldLocations) {
                    reset();
                }
                popSize.setText(String.valueOf(dotGame.getAliveCount(DotGame.DEFAULT_POPULATION)));
                dotGame.churn();
            }
        };
        new DrawingHandler(this);
    }

    private void drawBackground() {
        List<ImageObstacle> imageObstacles = Game.getImageObstacles();
        GraphicsContext graphicsContext2D = gameCanvasBackground.getGraphicsContext2D();
        PixelWriter pixelWriter = graphicsContext2D.getPixelWriter();
        for (ImageObstacle imageObstacle : imageObstacles) {
            Byte[][] image = imageObstacle.getImage();
            for (int x = 0; x < image.length; x++) {
                int lengthY = image[x].length;
                for (int y = 0; y < lengthY; y++) {
                    pixelWriter.setColor(x, y, image[x][y] <= Game.Setup.COLLISION_THRESHOLD ? Color.BLACK : Color.WHITE);
                }
            }
        }
        graphicsContext2D.setStroke(Color.RED);
        graphicsContext2D.setFill(Color.RED);
        graphicsContext2D.fillOval(Game.Setup.goal.x - 3d, Game.Setup.goal.y - 3d, 6d, 6d);
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
            currentColor = Color.BLUE;
            graphicsContext.setStroke(currentColor);
            graphicsContext.setFill(currentColor);
        } /*else if (currentColor != DEFAULT_COLOR) {
            currentColor = DEFAULT_COLOR;
            graphicsContext.setStroke(currentColor);
            graphicsContext.setFill(currentColor);
        } */ else {
            double hue = getHue(position);
            double saturation = 1d;//clamp(0d, position.y, Game.Setup.SCREEN_WIDTH - 1) * SATURATION_SCALE;
            currentColor = Color.hsb(hue, saturation, 1.0);//DEFAULT_POPULATION;
            graphicsContext.setStroke(currentColor);
            graphicsContext.setFill(currentColor);
        }
        graphicsContext.fillOval(position.x - 2d, position.y - 2d, 3d, 3d);
    }

    private double getHue(Vector2D position) {
        //fixme
        // this calculates the color based on the current location.
        // Perhaps we can have each dot base its color on some feature of its genome (a hash of sorts of the first gene?
        //  --- not currently available)

        //todo
        // in future the plan for this should be to calculate the value based on the distance to the current goal for
        // the population.
        //  --- currently this is only based on the vertical distance to the goal.

        return GREEN_HUE - position.y * GREEN_HUE_SCALING_FACTOR;
    }

    @Override
    public void updateStats(Status status) {
        boolean solved = status.isSolved();
        int stepsTaken = status.getStepsTaken();
        long stepsMax = status.getStepsMax();
        double maxFitness = status.getMaxFitness();
        double mutationRate = status.getMutationRate();
        double speed = status.getSpeed();
        int currentGeneration = status.getCurrentGeneration();
        int aliveCount = status.getAliveCount();
        int populationTotal = status.getPopulationCount();
        boolean truncPop = status.getTruncatePopulation();
        long randomSeed = status.getRandomSeed();
        this.solved.setSelected(solved);
        this.stepsTaken.setText(String.valueOf(stepsTaken));
        this.generation.setText(String.valueOf(currentGeneration));
        this.speed.setText(String.valueOf(speed));
        this.maxFitness.setText(String.format("%f", maxFitness));
        this.popSize.setText(String.valueOf(aliveCount));
        this.mutationRate.setText(String.format("%4.3f%%", mutationRate * 100d));
        System.out.printf(LOG_MESSAGE_FORMAT, solved ? "*" : " ", truncPop ? "-" : "=", currentGeneration, maxFitness, stepsTaken, stepsMax, this.speed, aliveCount, populationTotal, mutationRate, randomSeed);
        reset();
    }

    @Override
    public void reset() {
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        graphicsContext.setLineWidth(1);
        currentColor = DEFAULT_COLOR;
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

    @Override
    public DrawingViewWrapper getView() {
        return new DrawingViewWrapper(gameCanvas);
    }

    @Override
    public void startDraw(Vector2D point, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            backgroundGraphicsContext.setLineWidth(10);
            backgroundGraphicsContext.setStroke(OBSTACLE_COLOR);
            backgroundGraphicsContext.beginPath();
            backgroundGraphicsContext.moveTo(point.x, point.y);
            graphicsContext.stroke();
        }
    }

    @Override
    public void drawPath(Vector2D point, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            backgroundGraphicsContext.beginPath();
            backgroundGraphicsContext.lineTo(point.x, point.y);
            backgroundGraphicsContext.stroke();
        }
    }

    @Override
    public void complete(DrawingEvent mouseEvent, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            if (mouseEvent.isClickEvent()) {
                Vector2D location = mouseEvent.getHead();
                backgroundGraphicsContext.setFill(OBSTACLE_COLOR);
                backgroundGraphicsContext.fillRect(location.x - 5d, location.y - 5d, 10d, 10d);
                backgroundGraphicsContext.setFill(currentColor);
            } else {
                backgroundGraphicsContext.closePath();
            }

            WritableImage wim = new WritableImage(Game.Setup.SCREEN_WIDTH, Game.Setup.SCREEN_HEIGHT);
            gameCanvasBackground.snapshot(snapshotResult -> {
                Byte[][] image = ImageUtil.getImage(snapshotResult.getImage());
                Game.setExclusiveImageObstacle(new ImageObstacle(image));
                return null;
            }, null, wim);
        }

    }
}
