package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Part;

import java.time.LocalDate;

public class DialogWindowController {
    @FXML private Label dialogTitleLabel;
    @FXML private Label errorLabel;

    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField brandField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField categoryField;
    @FXML private TextField dateAddedField;
    @FXML private TextField imgPathField;
    @FXML private TextField thresholdField;

    private Part resultPart;

    public Part getResultPart() {
        return resultPart;
    }

    public void setExistingPart(Part existingPart) {
        dialogTitleLabel.setText("Update Item");
        codeField.setText(existingPart.getCode());
        codeField.setDisable(true);
        nameField.setText(existingPart.getName());
        brandField.setText(existingPart.getBrand());
        priceField.setText(String.valueOf(existingPart.getPrice()));
        quantityField.setText(String.valueOf(existingPart.getQty()));
        categoryField.setText(existingPart.getCategory());
        dateAddedField.setText(existingPart.getDateAdded());
        imgPathField.setText(existingPart.getImgPath());
        thresholdField.setText(String.valueOf(existingPart.getLowStockThreshold()));
    }

    @FXML
    public void initialize() {
        saveBtn.setOnAction(e -> handleSave());
        cancelBtn.setOnAction(e -> handleCancel());
    }

    public void handleSave() {
        if (codeField.getText().trim().isEmpty()) {
            errorLabel.setText("Code is required.");
            return;
        }
        if (nameField.getText().trim().isEmpty()) {
            errorLabel.setText("Name is required.");
            return;
        }
        if (categoryField.getText().trim().isEmpty()) {
            errorLabel.setText("Category is required.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            errorLabel.setText("Price must be a number.");
            return;
        }
        if (price <= 0) {
            errorLabel.setText("Price must be greater than 0.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            errorLabel.setText("Quantity must be a whole number.");
            return;
        }
        if (quantity < 0) {
            errorLabel.setText("Quantity cannot be negative.");
            return;
        }

        int threshold;
        try {
            threshold = Integer.parseInt(thresholdField.getText().trim());
        } catch (NumberFormatException e) {
            errorLabel.setText("Low-stock threshold must be a whole number.");
            return;
        }
        if (threshold < 0) {
            errorLabel.setText("Low-stock threshold cannot be negative.");
            return;
        }

        String brand = brandField.getText().trim().isEmpty() ? "unknown" : brandField.getText().trim();
        String dateAdded = dateAddedField.getText().trim().isEmpty() ? LocalDate.now().toString() : dateAddedField.getText().trim();
        String imgPath = imgPathField.getText().trim().isEmpty() ? "no_image.png" : imgPathField.getText().trim();

        resultPart = new Part(
                codeField.getText().trim(),
                nameField.getText().trim(),
                brand,
                price,
                quantity,
                categoryField.getText().trim().toLowerCase(),
                dateAdded,
                imgPath
        );
        resultPart.setLowStockThreshold(threshold);

        ((Stage) saveBtn.getScene().getWindow()).close();
    }

    private void handleCancel() {
        resultPart = null;
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }
}
