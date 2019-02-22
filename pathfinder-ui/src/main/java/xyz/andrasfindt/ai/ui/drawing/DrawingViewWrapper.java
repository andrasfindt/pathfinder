package xyz.andrasfindt.ai.ui.drawing;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

public class DrawingViewWrapper {
    private final Node view;

    public DrawingViewWrapper(Node view) {
        this.view = view;
    }

//    public Node getView() {
//        return view;
//    }

    public final <T extends Event> void addEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
        view.addEventHandler(eventType, eventHandler);
    }
}
