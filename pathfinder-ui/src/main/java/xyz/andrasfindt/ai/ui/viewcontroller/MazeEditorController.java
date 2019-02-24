package xyz.andrasfindt.ai.ui.viewcontroller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.BackgroundImageObstacle;
import xyz.andrasfindt.ai.ui.SceneController;
import xyz.andrasfindt.ai.ui.drawing.DrawingEvent;
import xyz.andrasfindt.ai.ui.drawing.DrawingHandler;
import xyz.andrasfindt.ai.ui.drawing.DrawingListener;
import xyz.andrasfindt.ai.ui.drawing.DrawingViewWrapper;
import xyz.andrasfindt.ai.ui.drawing.ImageUtil;

import javax.imageio.ImageIO;
import java.io.File;

public class MazeEditorController implements DrawingListener {
    @FXML
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    @FXML
    private void export(ActionEvent actionEvent) {
        WritableImage wim = new WritableImage(Game.Setup.SCREEN_WIDTH, Game.Setup.SCREEN_HEIGHT);

        File file = new File("CanvasImage.png");

        canvas.snapshot(snapshotResult -> {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshotResult.getImage(), null), "png", file);
            } catch (Exception s) {
            }
            return null;
        }, null, wim);

    }

    public void initialize() {
        graphicsContext = canvas.getGraphicsContext2D();

        initDraw();

        new DrawingHandler(this);

    }

    private void initDraw() {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);

        graphicsContext.fill();
        graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(10);
    }

    @FXML
    public void load(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            openFile(file);
        }
    }

    private void openFile(File file) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.fill();
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);
        graphicsContext.drawImage(new Image("file://" + absolutePath), 0, 0);
    }

    @FXML
    public void use(ActionEvent actionEvent) {
        WritableImage wim = new WritableImage(Game.Setup.SCREEN_WIDTH, Game.Setup.SCREEN_HEIGHT);
        canvas.snapshot(snapshotResult -> {
            Byte[][] image = ImageUtil.getImage(snapshotResult.getImage());
            Game.setBackgroundImageObstacle(new BackgroundImageObstacle(image));
            SceneController.setScene(SceneController.makeScene("gameMaze", "maze_game.fxml"));
            return null;
        }, null, wim);
    }

    @FXML
    public void clear(ActionEvent actionEvent) {
        graphicsContext.clearRect(0, 0, Game.Setup.SCREEN_WIDTH, Game.Setup.SCREEN_HEIGHT);
        initDraw();
    }

    @Override
    public DrawingViewWrapper getView() {
        return new DrawingViewWrapper(canvas);
    }

    @Override
    public void startDraw(Vector2D point, MouseButton button) {
        Color previousColor = assignTempDrawColor(button);
        graphicsContext.beginPath();
        graphicsContext.moveTo(point.x, point.y);
        graphicsContext.stroke();
        graphicsContext.setStroke(previousColor);
    }

    private Color assignTempDrawColor(MouseButton button) {
        Color previousColor = (Color) graphicsContext.getStroke();
        if (button == MouseButton.PRIMARY) {
            graphicsContext.setStroke(Color.BLACK);
        } else if (button == MouseButton.SECONDARY) {
            graphicsContext.setStroke(Color.WHITE);
        }
        return previousColor;
    }

    @Override
    public void drawPath(Vector2D point, MouseButton button) {
        Color previousColor = assignTempDrawColor(button);
        graphicsContext.lineTo(point.x, point.y);
        graphicsContext.stroke();
        graphicsContext.setStroke(previousColor);
    }

    @Override
    public void complete(DrawingEvent mouseEvent, MouseButton button) {
        if (mouseEvent.isClickEvent()) {
            Vector2D location = mouseEvent.getHead();
            if (button == MouseButton.PRIMARY) {
                graphicsContext.setFill(Color.BLACK);
                graphicsContext.fillRect(location.x - 5d, location.y - 5d, 10d, 10d);
                graphicsContext.setFill(Color.WHITE);
            } else if (button == MouseButton.SECONDARY) {
                graphicsContext.setFill(Color.WHITE);
                graphicsContext.fillRect(location.x - 5d, location.y - 5d, 10d, 10d);
                graphicsContext.setFill(Color.BLACK);
            }
        } else {
            graphicsContext.closePath();
        }
    }
}