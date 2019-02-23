package xyz.andrasfindt.ai.ui.drawing;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import xyz.andrasfindt.ai.Game;

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
}
