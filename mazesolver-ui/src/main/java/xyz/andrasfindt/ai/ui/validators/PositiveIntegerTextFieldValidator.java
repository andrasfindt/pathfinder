package xyz.andrasfindt.ai.ui.validators;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class PositiveIntegerTextFieldValidator implements EventHandler<javafx.event.ActionEvent>, ChangeListener<Boolean> {
    private static final BigInteger MAX_INT = BigInteger.valueOf(Integer.MAX_VALUE);
    private TextField textField;

    public PositiveIntegerTextFieldValidator(@NotNull TextField textField) {
        this.textField = textField;
        this.textField.focusedProperty().addListener(this);
        this.textField.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        actionEvent.consume();
        validateNumberInput(textField);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldFocus, Boolean newFocus) {
        if (!newFocus) {
            validateNumberInput(textField);
        }
    }

    private void validateNumberInput(TextField textField) {
        String[] split = textField.getText().split("[^\\d+]+");
        String sb = String.join("", split);
        BigInteger temp = new BigInteger(sb);
        if (MAX_INT.compareTo(temp) >= 0) {
            textField.setText(sb);
        }
        textField.setText(sb);
    }
}
