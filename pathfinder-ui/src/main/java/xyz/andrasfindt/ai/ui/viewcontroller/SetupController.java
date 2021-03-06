package xyz.andrasfindt.ai.ui.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    public CheckBox optimizeSteps;
    @FXML
    public TextField genomeSize;
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
        Game.Setup.POPULATION_SIZE = Integer.valueOf(populationSize.getText());
        Game.Setup.MUTATION_RATE = Double.valueOf(mutationRate.getText());
        Game.Setup.RANDOM_SEED = Long.valueOf(randomSeed.getText());
        Game.Setup.TRUNCATE_POPULATION = optimizeSteps.isSelected();
        Game.Setup.DEFAULT_GENOME_SIZE = Integer.valueOf(genomeSize.getText());
        SceneController.setScene("editMaze");
    }

    public void initialize() {
        populationSizeLabel.setText("PopulationSize");
        populationSize.setText(String.valueOf(Game.Setup.POPULATION_SIZE));
        mutationRate.setText(String.valueOf(Game.Setup.MUTATION_RATE));
        randomSeed.setText(String.valueOf(Game.Setup.RANDOM_SEED));
        optimizeSteps.setSelected(Game.Setup.TRUNCATE_POPULATION);
        genomeSize.setText(String.valueOf(Game.Setup.DEFAULT_GENOME_SIZE));

        new PositiveIntegerTextFieldValidator(populationSize);
        new PositiveIntegerTextFieldValidator(width);
        new PositiveIntegerTextFieldValidator(height);
        new PositiveIntegerTextFieldValidator(genomeSize);
        new PositiveLongTextFieldValidator(randomSeed);
        new PositiveDoubleTextFieldValidator(mutationRate);
    }
}
