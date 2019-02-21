package xyz.andrasfindt.ai;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xyz.andrasfindt.ai.ui.SceneController;

public class Main extends Application {


    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneController.init(stage);
        stage.setTitle("Solve thingle.");

        Scene scene = SceneController.makeScene("setup", "setup.fxml");
        SceneController.makeScene("editMaze", "maze_editor.fxml");
        SceneController.setScene(scene);
    }

}
