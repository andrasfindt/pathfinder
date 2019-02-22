package xyz.andrasfindt.ai.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xyz.andrasfindt.ai.Main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneController {

    private static final SceneController instance = new SceneController();
    private Map<String, Scene> scenes = new HashMap<>();
    private Stage primaryStage;

    private SceneController() {

    }

    public static void init(Stage primaryStage) {
        instance.primaryStage = primaryStage;
    }

    public static void setScene(String id) {
        setScene(instance.scenes.get(id));
    }

    public static void putScene(String id, Scene scene) {
        instance.scenes.put(id, scene);
    }

    public static void setScene(Scene scene) {
        instance.primaryStage.hide();
        instance.primaryStage.setScene(scene);
        instance.primaryStage.show();
    }

    public static Scene makeScene(String sceneId, String resourceName) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(resourceName));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
            SceneController.putScene(sceneId, scene);
            return scene;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
