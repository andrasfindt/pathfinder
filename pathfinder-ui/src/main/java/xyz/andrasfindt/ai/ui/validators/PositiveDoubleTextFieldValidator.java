package xyz.andrasfindt.ai.ui.validators;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class PositiveDoubleTextFieldValidator implements EventHandler<ActionEvent>, ChangeListener<Boolean> {
    private TextField textField;

    public PositiveDoubleTextFieldValidator(TextField textField) {
        this.textField = textField;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldFocus, Boolean newFocus) {
        if (!newFocus) {
            validateNumberInput(textField);
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        actionEvent.consume();
        validateNumberInput(textField);
    }

    private void validateNumberInput(TextField textField) {
        try {
            double v = Double.parseDouble(textField.getText());
            textField.setText(String.valueOf(v));
        } catch (NumberFormatException e) {
            String[] split = textField.getText().split("[^\\d+|.]+");
            String sb = String.join("", split);
            textField.setText(sb);
        }
    }
}
