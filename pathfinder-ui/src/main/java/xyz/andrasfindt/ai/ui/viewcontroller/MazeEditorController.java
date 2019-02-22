package xyz.andrasfindt.ai.ui.viewcontroller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.obstacles.ImageObstacle;
import xyz.andrasfindt.ai.ui.SceneController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class MazeEditorController {

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

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
        });

    }

    private void initDraw() {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);

        graphicsContext.fill();
        graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);

        graphicsContext.setFill(Color.LIGHTGRAY);
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
            byte[][] image = getImage(snapshotResult.getImage());
            Game.setExclusiveImageObstacle(new ImageObstacle(image));
            SceneController.makeScene("gameMaze", "maze_game.fxml");
            SceneController.setScene("gameMaze");
            return null;
        }, null, wim);
    }

    private byte[][] getImage(WritableImage image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        byte[][] imageBytes = new byte[Game.Setup.SCREEN_HEIGHT][];
        for (int x = 0; x < Game.Setup.SCREEN_HEIGHT; x++) {
            imageBytes[x] = new byte[Game.Setup.SCREEN_WIDTH];
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
}