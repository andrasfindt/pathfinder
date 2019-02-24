package xyz.andrasfindt.ai.ui.drawing;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.PlayerImageObstacle;

import java.awt.image.BufferedImage;

public class ImageUtil {

    public static Byte[][] getImage(WritableImage image) {
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

    public static void writeImageToCanvas(PixelWriter pixelWriter, Byte[][] image, Color color) {
        for (int x = 0; x < image.length; x++) {
            int lengthY = image[x].length;
            for (int y = 0; y < lengthY; y++) {
                if (image[x][y] <= Game.Setup.COLLISION_THRESHOLD) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
    }

    public static void writeObstacleImageToCanvas(PixelWriter pixelWriter, PlayerImageObstacle imageObstacle, Color color) {
        Byte[][] image = imageObstacle.getImage();
        Vector2D start = imageObstacle.getBoundingBox().start;
        for (int x = 0; x < image.length; x++) {
            int lengthY = image[x].length;
            for (int y = 0; y < lengthY; y++) {
                if (image[x][y] <= Game.Setup.COLLISION_THRESHOLD) {
                    pixelWriter.setColor((int) (x + start.x), (int) (y + start.y), color);
                }
            }
        }
    }
}
