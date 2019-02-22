package xyz.andrasfindt.ai.ui.viewcontroller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.ImageObstacle;
import xyz.andrasfindt.ai.ui.SceneController;
import xyz.andrasfindt.ai.ui.drawing.DrawingEvent;
import xyz.andrasfindt.ai.ui.drawing.DrawingHandler;
import xyz.andrasfindt.ai.ui.drawing.DrawingListener;
import xyz.andrasfindt.ai.ui.drawing.DrawingViewWrapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MazeEditorController implements DrawingListener {

    List<Vector2D> mouseEvent = new ArrayList<>();
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
            Byte[][] image = getImage(snapshotResult.getImage());
            Game.setExclusiveImageObstacle(new ImageObstacle(image));
            SceneController.setScene(SceneController.makeScene("gameMaze", "maze_game.fxml"));
            return null;
        }, null, wim);
    }

    private Byte[][] getImage(WritableImage image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        Byte[][] imageBytes = new Byte[Game.Setup.SCREEN_HEIGHT][];
        for (int x = 0; x < Game.Setup.SCREEN_HEIGHT; x++) {
            imageBytes[x] = new Byte[Game.Setup.SCREEN_WIDTH];
            for (int y = 0; y < Game.Setup.SCREEN_WIDTH; y++) {
                int pixel = bufferedImage.getRGB(x, y);

                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                byte grayLevel = (byte) ((0.215 * red + 0.714 * green + 0.071 * blue) / 2);
                imageBytes[x][y] = grayLevel;
            }
        }
        return imageBytes;
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
    public void startDraw(Vector2D point) {
        graphicsContext.beginPath();
        graphicsContext.moveTo(point.x, point.y);
        graphicsContext.stroke();

    }

    @Override
    public void drawPath(Vector2D point) {
        graphicsContext.lineTo(point.x, point.y);
        graphicsContext.stroke();
    }

    @Override
    public void complete(DrawingEvent mouseEvent) {
        if (mouseEvent.isClickEvent()) {
            Vector2D location = mouseEvent.getHead();
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(location.x - 5d, location.y - 5d, 10d, 10d);
            graphicsContext.setFill(Color.WHITE);
        } else {
            graphicsContext.closePath();
        }
    }
}