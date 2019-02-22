package xyz.andrasfindt.ai.ui.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.ui.SceneController;
import xyz.andrasfindt.ai.ui.validators.PositiveDoubleTextFieldValidator;
import xyz.andrasfindt.ai.ui.validators.PositiveIntegerTextFieldValidator;
import xyz.andrasfindt.ai.ui.validators.PositiveLongTextFieldValidator;

public class SetupController {
    @FXML
    public TextField mutationRate;
    @FXML
    public TextField randomSeed;
    @FXML
    private TextField populationSize;
    @FXML
    private TextField width;
    @FXML
    private TextField height;
    @FXML
    private Label populationSizeLabel;

    public SetupController() {
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Game.POPULATION_SIZE = Integer.valueOf(populationSize.getText());
        Game.MUTATION_RATE = Double.valueOf(mutationRate.getText());
        Game.RANDOM_SEED = Long.valueOf(randomSeed.getText());
        SceneController.setScene("editMaze");
    }

    public void initialize() {
        populationSizeLabel.setText("PopulationSize");
        populationSize.setText(String.valueOf(Game.POPULATION_SIZE));
        mutationRate.setText(String.valueOf(Game.MUTATION_RATE));
        randomSeed.setText(String.valueOf(Game.RANDOM_SEED));
        new PositiveIntegerTextFieldValidator(populationSize);
        new PositiveIntegerTextFieldValidator(width);
        new PositiveIntegerTextFieldValidator(height);
        new PositiveLongTextFieldValidator(randomSeed);
        new PositiveDoubleTextFieldValidator(mutationRate);
    }
}
