package xyz.andrasfindt.ai.obstacle;

import xyz.andrasfindt.ai.geom.Rectangle2D;

public class BarrierObstacle extends DestroyableObstacle {

    private static final int DEFAULT_HEALTH = 100;

    public BarrierObstacle(Byte[][] image) {
        super(image, DEFAULT_HEALTH);
    }

    public BarrierObstacle(Byte[][] image, Rectangle2D boundingBox) {
        super(image, boundingBox, DEFAULT_HEALTH);
    }
}
