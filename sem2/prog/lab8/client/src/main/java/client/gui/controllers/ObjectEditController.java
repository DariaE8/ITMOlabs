package client.gui.controllers;

import models.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ObjectEditController {

    @FXML private TextField nameField;
    @FXML private TextField xField;
    @FXML private TextField yField;
    @FXML private Button saveButton;

    private Ticket ticket;
    private boolean confirmed = false;

    public void initializeFields(Ticket ticket) {
        this.ticket = ticket;
        nameField.setText(ticket.getName());
        xField.setText(String.valueOf(ticket.getCoordinates().getX()));
        yField.setText(String.valueOf(ticket.getCoordinates().getY()));
    }

    public Ticket getUpdatedTicket() {
        // ticket.setName(nameField.getText());
        // ticket.getCoordinates().setX(Float.parseFloat(xField.getText()));
        // ticket.getCoordinates().setY(Float.parseFloat(yField.getText()));
        return ticket;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void onSave() {
        confirmed = true;
        ((Stage) saveButton.getScene().getWindow()).close();
    }

    @FXML
    private void onCancel() {
        ((Stage) saveButton.getScene().getWindow()).close();
    }
}
