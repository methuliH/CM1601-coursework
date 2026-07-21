package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Part;
import java.time.LocalDate;

public class AddPartDialogController {
    @FXML private TextField codeField, nameField, brandField, priceField, quantityField, categoryField;
    @FXML private Label errorLabel;
    @FXML private Button saveBtn, cancelBtn;

    private Part resultPart = null;
    private boolean saved = false;
    private String originalCode = null;

    @FXML
    private void handleSave() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String brand = brandField.getText().trim();
        String category = categoryField.getText().trim();

        if (code.isEmpty() || name.isEmpty()) {
            errorLabel.setText("Part code and name cannot be empty");
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceField.getText().trim());
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            errorLabel.setText("Price must be a number and quantity must be a whole number");
            return;
        }

        String dateAdded = LocalDate.now().toString();
        String imgPath = "";

        resultPart = new Part(code, name, brand, price, quantity, category, dateAdded, imgPath);
        saved = true;
        closeStage();
    }

    @FXML
    private void handleCancel() {
        saved = false;
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    public Part getResultPart() {
        return resultPart;
    }

    public boolean isSaved() {
        return saved;
    }

    //SET TO EDIT
    public void setPartToEdit(Part part) {
        this.originalCode = part.getCode();
        codeField.setText(part.getCode());
        codeField.setDisable(true); // code can't be changed once created
        nameField.setText(part.getName());
        brandField.setText(part.getBrand());
        priceField.setText(String.valueOf(part.getPrice()));
        quantityField.setText(String.valueOf(part.getQty()));
        categoryField.setText(part.getCategory());
    }

    public String getOriginalCode() {
        return originalCode;
    }

}
