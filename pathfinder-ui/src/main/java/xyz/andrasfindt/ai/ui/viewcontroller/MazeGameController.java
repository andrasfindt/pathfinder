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
import xyz.andrasfindt.ai.geom.Rectangle2D;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.BackgroundImageObstacle;
import xyz.andrasfindt.ai.obstacle.PlayerImageObstacle;
import xyz.andrasfindt.ai.ui.drawing.DrawingEvent;
import xyz.andrasfindt.ai.ui.drawing.DrawingHandler;
import xyz.andrasfindt.ai.ui.drawing.DrawingListener;
import xyz.andrasfindt.ai.ui.drawing.DrawingViewWrapper;
import xyz.andrasfindt.ai.ui.drawing.ImageUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalDouble;

public class MazeGameController implements Listener, DrawingListener {
    private static final Color DEFAULT_TEXT_COLOR = Color.LIGHTCYAN;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private static final double GREEN_HUE = Color.GREEN.getHue();
    private static final double GREEN_HUE_SCALING_FACTOR = GREEN_HUE / (Game.Setup.SCREEN_HEIGHT - Game.Setup.goal.y / 2d - 1d);
    private static final Color OBSTACLE_COLOR = Color.SADDLEBROWN;
    private static final String LOG_MESSAGE_FORMAT = "%s %s gen: %d max: %s step(t/m): %d/%d max_speed: %s creeps(a/p): %d/%d m-rate: %s r-seed: %d d-goal: %s";
    private static final Color DEFAULT_COLOR = Color.hsb(0d, 1d, 1d);//Color.BLUEVIOLET;
    @FXML
    public Label popSize;
    @FXML
    public Canvas gameCanvasGoals;
    @FXML
    public Canvas gameCanvasObstacles;
    @FXML
    public Canvas gameCanvasStatistics;
    DecimalFormat df = new DecimalFormat("#.##########");
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
    private boolean goalsNeedUpdating = true;
    private Color STATISTICS_BACKGROUND_COLOR = Color.color(0d, 0d, 0d, .3333d);

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
        if (obstaclesNeedUpdating) {
            obstaclesGraphicsContext.setStroke(OBSTACLE_COLOR);
            obstaclesGraphicsContext.setFill(OBSTACLE_COLOR);
            obstaclesGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            obstaclesNeedUpdating = false;
            List<PlayerImageObstacle> imageObstacles = Game.getPlayerImageObstacles();
            PixelWriter pixelWriter = obstaclesGraphicsContext.getPixelWriter();
            for (PlayerImageObstacle imageObstacle : imageObstacles) {
                ImageUtil.writeObstacleImageToCanvas(pixelWriter, imageObstacle, OBSTACLE_COLOR);
            }
        }
    }

    private void drawGoals() {
        if (goalsNeedUpdating) {
            goalsGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            goalsGraphicsContext.setStroke(Color.RED);
            goalsGraphicsContext.setFill(Color.RED);
            goalsGraphicsContext.fillOval(Game.Setup.goal.x - 3d, Game.Setup.goal.y - 3d, 7d, 7d);
            goalsNeedUpdating = false;
        }
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
        double goalDistance = status.getDisanceFromGoal();
        String truncPopIndicator = truncPop ? "-" : "=";
        String statsLogMessage = String.format(LOG_MESSAGE_FORMAT, solved ? "*" : " ", truncPopIndicator, currentGeneration,
                maxFitness, stepsTaken, stepsMax, speed, aliveCount, populationTotal, mutationRate, randomSeed, goalDistance);
        System.out.println(statsLogMessage);

        statisticsGraphicsContext.clearRect(0d, 0d, canvasWidth - 1d, canvasHeight - 1d);
        statisticsGraphicsContext.setStroke(DEFAULT_TEXT_COLOR);
        statisticsGraphicsContext.setFill(STATISTICS_BACKGROUND_COLOR);
        statisticsGraphicsContext.fillRect(0, canvasHeight - 148d, 180d, 140d);
        double startPadding = 8d;
        statisticsGraphicsContext.strokeText(String.format("gen: %d | %s", currentGeneration, truncPopIndicator), startPadding, canvasHeight - 16d);
        statisticsGraphicsContext.strokeText(String.format("solved: %s", solved ? "yes" : "no"), startPadding, canvasHeight - 32d);
        statisticsGraphicsContext.strokeText(String.format("steps: %d/%d", stepsTaken, stepsMax), startPadding, canvasHeight - 48d);
        statisticsGraphicsContext.strokeText(String.format("pop: %d", populationTotal), startPadding, canvasHeight - 64d);
        statisticsGraphicsContext.strokeText(String.format("m-rate: %s", df.format(mutationRate)), startPadding, canvasHeight - 80d);
        statisticsGraphicsContext.strokeText(String.format("fitness: %s", df.format(maxFitness)), startPadding, canvasHeight - 96d);
        statisticsGraphicsContext.strokeText(String.format("speed: %s", df.format(speed)), startPadding, canvasHeight - 112d);
        statisticsGraphicsContext.strokeText(String.format("d-goal: %s", df.format(goalDistance)), startPadding, canvasHeight - 128d);
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
            Rectangle2D boundingBox;
            if (mouseEvent.isClickEvent()) {
                Vector2D location = mouseEvent.getHead();
                obstaclesGraphicsContext.setFill(OBSTACLE_COLOR);
                boundingBox = new Rectangle2D(location.x - 5d, location.y - 5d, 10d, 10d);
                obstaclesGraphicsContext.fillRect(boundingBox.start.x, boundingBox.start.y, boundingBox.width, boundingBox.height);
            } else {
                OptionalDouble minX = mouseEvent.getPoints().stream().mapToDouble(s -> s.x).min();
                OptionalDouble minY = mouseEvent.getPoints().stream().mapToDouble(s -> s.y).min();
                OptionalDouble maxX = mouseEvent.getPoints().stream().mapToDouble(s -> s.x).max();
                OptionalDouble maxY = mouseEvent.getPoints().stream().mapToDouble(s -> s.y).max();
                double x = minX.isPresent() ? Math.max(0d, minX.getAsDouble() - 5d) : (0d);
                double y = minY.isPresent() ? Math.max(0d, minY.getAsDouble() - 5d) : (0d);
                double x1 = maxX.isPresent() ? Math.min(canvasWidth - 1, maxX.getAsDouble() + 5d) : (0d);
                double y1 = maxY.isPresent() ? Math.min(canvasHeight - 1, maxY.getAsDouble() + 5d) : (0d);
                boundingBox = new Rectangle2D(new Vector2D(x, y), new Vector2D(x1, y1));
                obstaclesGraphicsContext.closePath();
            }

            WritableImage wim = new WritableImage(Game.Setup.SCREEN_WIDTH, Game.Setup.SCREEN_HEIGHT);
            gameCanvasObstacles.snapshot(snapshotResult -> {
                Byte[][] image = ImageUtil.getImage(snapshotResult.getImage());
                Game.getObstacles().add(new PlayerImageObstacle(image, boundingBox));
                obstaclesNeedUpdating = true;
                return null;
            }, null, wim);
        }

    }
}
