package xyz.andrasfindt.ai.ui.creep;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import xyz.andrasfindt.ai.creep.BaseCreep;
import xyz.andrasfindt.ai.geom.Vector2D;

public class CreepDot extends Circle {
    public CreepDot(double radius, Vector2D position, BaseCreep.ViewModel.Paint color) {
        super(position.x, position.y, radius);
        setFill(Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness()));
        setStroke(Color.BLACK);
        setStrokeWidth(.5d);
    }

    public void update(Vector2D position, BaseCreep.ViewModel.Paint color) {
        setFill(Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness()));
        setTranslateX(position.x - getCenterX());
        setTranslateY(position.y - getCenterY());
    }
}
