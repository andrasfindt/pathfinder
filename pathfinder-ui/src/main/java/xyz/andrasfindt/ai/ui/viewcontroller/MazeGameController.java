package xyz.andrasfindt.ai.ui.viewcontroller;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import xyz.andrasfindt.ai.obstacle.BackgroundImageObstacle;
import xyz.andrasfindt.ai.obstacle.PlayerImageObstacle;
import xyz.andrasfindt.ai.ui.drawing.DrawingEvent;
import xyz.andrasfindt.ai.ui.drawing.DrawingHandler;
import xyz.andrasfindt.ai.ui.drawing.DrawingListener;
import xyz.andrasfindt.ai.ui.drawing.DrawingViewWrapper;
import xyz.andrasfindt.ai.ui.drawing.ImageUtil;

import java.util.List;

public class MazeGameController implements Listener, DrawingListener {
    public static final Color DEFAULT_TEXT_COLOR = Color.BLUEVIOLET;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private static final double GREEN_HUE = Color.GREEN.getHue();
    private static final double GREEN_HUE_SCALING_FACTOR = GREEN_HUE / (Game.Setup.SCREEN_HEIGHT - Game.Setup.goal.y / 2d - 1d);
    private static final Color OBSTACLE_COLOR = Color.SADDLEBROWN;
    private static final String LOG_MESSAGE_FORMAT = "%s %s gen: %d max: %s step(t/m): %d/%d max_speed: %s creeps(a/p): %d/%d m-rate: %s r-seed: %d";
    private static final Color DEFAULT_COLOR = Color.hsb(0d, 1d, 1d);//Color.BLUEVIOLET;
    @FXML
    public Label popSize;
    @FXML
    public Canvas gameCanvasGoals;
    @FXML
    public Canvas gameCanvasObstacles;
    @FXML
    public Canvas gameCanvasStatistics;
    @FXML
    private Canvas gameCanvasCreeps;
    @FXML
    private Canvas gameCanvasBackground;

    private GraphicsContext creepsGraphicsContext;
    private GraphicsContext goalsGraphicsContext;
    private GraphicsContext obstaclesGraphicsContext;
    private GraphicsContext backgroundGraphicsContext;
    private GraphicsContext statisticsGraphicsContext;
    private double canvasWidth;
    private double canvasHeight;
    private Color currentColor = Color.BLACK;
    private AnimationTimer animator;
    private DotGame dotGame;
    private boolean drawOldLocations = false;
    private boolean obstaclesNeedUpdating = true;

    public void initialize() {
        creepsGraphicsContext = gameCanvasCreeps.getGraphicsContext2D();
        backgroundGraphicsContext = gameCanvasBackground.getGraphicsContext2D();
        goalsGraphicsContext = gameCanvasGoals.getGraphicsContext2D();
        obstaclesGraphicsContext = gameCanvasObstacles.getGraphicsContext2D();
        statisticsGraphicsContext = gameCanvasStatistics.getGraphicsContext2D();
        canvasWidth = creepsGraphicsContext.getCanvas().getWidth();
        canvasHeight = creepsGraphicsContext.getCanvas().getHeight();
        drawBackground();
        initDraw();
        popSize.setText(String.valueOf(Game.Setup.POPULATION_SIZE));

        dotGame = new DotGame(this);
        creepsGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                if (!drawOldLocations) {
                    reset();
                }
                popSize.setText(String.valueOf(dotGame.getAliveCount(DotGame.DEFAULT_POPULATION)));
                drawObstaclesIfNeeded();
                dotGame.churn();
            }
        };
        new DrawingHandler(this);
    }

    private void drawObstaclesIfNeeded() {
        //fixme
        // for now we are not clearing the obstacle layer on each pop update.
        // the reason is that we are not currently destroying any obstacles.
        // as it stands, clearing the canvas on each pop update means that any
        // ongoing mouse drag event breaks the writing image to Byte[][] process.
        //

        if (obstaclesNeedUpdating) {
            obstaclesGraphicsContext.setStroke(OBSTACLE_COLOR);
            obstaclesGraphicsContext.setFill(OBSTACLE_COLOR);
            obstaclesGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            obstaclesNeedUpdating = false;
            List<PlayerImageObstacle> imageObstacles = Game.getPlayerImageObstacles();
            PixelWriter pixelWriter = obstaclesGraphicsContext.getPixelWriter();
            for (PlayerImageObstacle imageObstacle : imageObstacles) {
                ImageUtil.writeImageToCanvas(pixelWriter, imageObstacle.getImage(), OBSTACLE_COLOR);
            }
        }
    }

    private void drawGoals() {
        goalsGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        goalsGraphicsContext.setStroke(Color.RED);
        goalsGraphicsContext.setFill(Color.RED);
        goalsGraphicsContext.fillOval(Game.Setup.goal.x - 3d, Game.Setup.goal.y - 3d, 6d, 6d);
    }

    private void drawBackground() {
        List<BackgroundImageObstacle> imageObstacles = Game.getBackgroundImageObstacles();
        PixelWriter pixelWriter = backgroundGraphicsContext.getPixelWriter();
        for (BackgroundImageObstacle imageObstacle : imageObstacles) {
            ImageUtil.writeImageToCanvas(pixelWriter, imageObstacle.getImage(), DEFAULT_BACKGROUND_COLOR);
        }
    }

    private void initDraw() {
        drawGoals();
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
            creepsGraphicsContext.setStroke(currentColor);
            creepsGraphicsContext.setFill(currentColor);
        } /*else if (currentColor != DEFAULT_COLOR) {
            currentColor = DEFAULT_COLOR;
            creepsGraphicsContext.setStroke(currentColor);
            creepsGraphicsContext.setFill(currentColor);
        } */ else {
            double hue = getHue(position);
            double saturation = 1d;
            currentColor = Color.hsb(hue, saturation, 1.0);
            creepsGraphicsContext.setStroke(currentColor);
            creepsGraphicsContext.setFill(currentColor);
        }
        creepsGraphicsContext.fillOval(position.x - 2d, position.y - 2d, 3d, 3d);
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
        String truncPopIndicator = truncPop ? "-" : "=";
        String statsLogMessage = String.format(LOG_MESSAGE_FORMAT, solved ? "*" : " ", truncPopIndicator, currentGeneration,
                maxFitness, stepsTaken, stepsMax, speed, aliveCount, populationTotal, mutationRate, randomSeed);
        System.out.println(statsLogMessage);

        statisticsGraphicsContext.clearRect(0d, 0d, canvasWidth - 1d, canvasHeight - 1d);
        statisticsGraphicsContext.setStroke(DEFAULT_TEXT_COLOR);
        statisticsGraphicsContext.strokeText(String.format("gen: %d | %s", currentGeneration, truncPopIndicator), 20d, canvasHeight - 16d);
        statisticsGraphicsContext.strokeText(String.format("solved: %s", solved ? "yes" : "no"), 20d, canvasHeight - 32d);
        statisticsGraphicsContext.strokeText(String.format("steps: %d/%d", stepsTaken, stepsMax), 20d, canvasHeight - 48d);
        statisticsGraphicsContext.strokeText(String.format("pop: %d", populationTotal), 20d, canvasHeight - 64d);
        statisticsGraphicsContext.strokeText(String.format("m-rate: %s", mutationRate), 20d, canvasHeight - 80d);
        statisticsGraphicsContext.strokeText(String.format("fitness: %s", maxFitness), 20d, canvasHeight - 96d);
        reset();
        drawGoals();
    }

    @Override
    public void reset() {
        creepsGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        creepsGraphicsContext.setLineWidth(1);
        currentColor = DEFAULT_COLOR;
        creepsGraphicsContext.setStroke(currentColor);
        creepsGraphicsContext.setFill(currentColor);
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
        return new DrawingViewWrapper(gameCanvasObstacles);
    }

    @Override
    public void startDraw(Vector2D point, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            obstaclesGraphicsContext.setLineWidth(10);
            obstaclesGraphicsContext.setStroke(OBSTACLE_COLOR);
            obstaclesGraphicsContext.beginPath();
            obstaclesGraphicsContext.moveTo(point.x, point.y);
            obstaclesGraphicsContext.stroke();
        }
    }

    @Override
    public void drawPath(Vector2D point, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            obstaclesGraphicsContext.beginPath();
            obstaclesGraphicsContext.lineTo(point.x, point.y);
            obstaclesGraphicsContext.stroke();
        }
    }

    @Override
    public void complete(DrawingEvent mouseEvent, MouseButton button) {
        if (button == MouseButton.PRIMARY) {
            if (mouseEvent.isClickEvent()) {
                Vector2D location = mouseEvent.getHead();
                obstaclesGraphicsContext.setFill(OBSTACLE_COLOR);
                obstaclesGraphicsContext.fillRect(location.x - 5d, location.y - 5d, 10d, 10d);
            } else {
                obstaclesGraphicsContext.closePath();
            }

            WritableImage wim = new WritableImage(Game.Setup.SCREEN_WIDTH, Game.Setup.SCREEN_HEIGHT);
            gameCanvasObstacles.snapshot(snapshotResult -> {
                Byte[][] image = ImageUtil.getImage(snapshotResult.getImage());
                Game.getObstacles().add(new PlayerImageObstacle(image));
                obstaclesNeedUpdating = true;
                return null;
            }, null, wim);
        }

    }
}
