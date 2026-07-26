package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Part;
import utils.InventoryManager;

import java.util.ArrayList;
import java.util.List;

public class InventoryTabController {
    private static final String ALL_CATEGORIES = "All Categories";

    private InventoryManager inventoryManager;

    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @FXML private Button inventoryAddPartBtn;
    @FXML private Button updateSelectedBtn;
    @FXML private Button deleteSelectedBtn;
    @FXML private Button saveThresholdBtn;

    @FXML private TextField keywordField;
    @FXML private ComboBox<String> searchCategoryField;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Button runSearchBtn;
    @FXML private Button clearSearchBtn;

    @FXML private Label inventoryTotalItemsLabel;
    @FXML private Label inventoryTotalValueLabel;

    @FXML private TableView<Part> inventoryTable;
    @FXML private TableColumn<Part, String> codeColumn;
    @FXML private TableColumn<Part, String> nameColumn;
    @FXML private TableColumn<Part, String> brandColumn;
    @FXML private TableColumn<Part, String> categoryColumn;
    @FXML private TableColumn<Part, String> priceColumn;
    @FXML private TableColumn<Part, Integer> qtyColumn;
    @FXML private TableColumn<Part, String> dateAddedColumn;
    @FXML private TableColumn<Part, Integer> thresholdColumn;

    @FXML private TextField thresholdField;

    public void setupInventoryTable(){
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("Rs %.2f", cellData.getValue().getPrice())));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        dateAddedColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        thresholdColumn.setCellValueFactory(new PropertyValueFactory<>("lowStockThreshold"));

    }
    public void refreshInventoryTab(){
        List<Part> sortedParts = inventoryManager.getAllPartsSortedByCategory();
        inventoryTable.setItems(FXCollections.observableArrayList(sortedParts));

        inventoryTotalItemsLabel.setText("Total Items: " + inventoryManager.getTotalItemCount());
        inventoryTotalValueLabel.setText(String.format("Total Value: Rs %.2f", inventoryManager.getTotalInventoryValue()));

        populateCategoryDropdown();
    }

    // Keeps the category filter's options in sync with what's actually in the
    // inventory (e.g. after adding a part with a brand-new category), while
    // preserving whatever the user currently has selected if it's still valid.
    private void populateCategoryDropdown() {
        String currentSelection = searchCategoryField.getValue();

        List<String> options = new ArrayList<>();
        options.add(ALL_CATEGORIES);
        options.addAll(inventoryManager.getAllCategories());
        searchCategoryField.setItems(FXCollections.observableArrayList(options));

        if (currentSelection != null && options.contains(currentSelection)) {
            searchCategoryField.setValue(currentSelection);
        } else {
            searchCategoryField.setValue(ALL_CATEGORIES);
        }
    }

    private Part openPartDialog(Part existingPart) throws IOException{
        FXMLLoader loader = new
        FXMLLoader(getClass().getResource("/gui/DialogWindow.fxml"));
        Parent root =loader.load();
        DialogWindowController controller = loader.getController();

        if(existingPart != null){
            controller.setExistingPart(existingPart);
        }
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();

        return controller.getResultPart();

    }

    @FXML
    public void initialize() {
        inventoryAddPartBtn.setOnAction(e -> handleAddPart());
        updateSelectedBtn.setOnAction(e -> handleUpdateSelected());
        deleteSelectedBtn.setOnAction(e -> handleDeleteSelected());
        saveThresholdBtn.setOnAction(e -> handleSaveThreshold());
        runSearchBtn.setOnAction(e -> handleSearch());
        clearSearchBtn.setOnAction(e -> handleClear());
    }

    private void handleSearch() {
        String keyword = keywordField.getText().trim();
        String selectedCategory = searchCategoryField.getValue();
        String category = (selectedCategory == null || selectedCategory.equals(ALL_CATEGORIES)) ? "" : selectedCategory;

        double minPrice;
        double maxPrice;
        try {
            minPrice = minPriceField.getText().trim().isEmpty() ? 0 : Double.parseDouble(minPriceField.getText().trim());
            maxPrice = maxPriceField.getText().trim().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid price", "Min/Max price must be numbers.");
            return;
        }

        List<Part> results = inventoryManager.search(keyword, category, minPrice, maxPrice);
        inventoryTable.setItems(FXCollections.observableArrayList(results));
    }

    private void handleClear() {
        keywordField.clear();
        searchCategoryField.setValue(ALL_CATEGORIES);
        minPriceField.clear();
        maxPriceField.clear();
        refreshInventoryTab();
    }

    public void handleAddPart() {
        try {
            Part result = openPartDialog(null);
            if (result == null) {
                return;
            }
            boolean added = inventoryManager.addPart(result);
            if (!added) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Code", "A part with code " + result.getCode() + " already exists.");
                return;
            }
            refreshInventoryTab();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the Add Part dialog: " + e.getMessage());
        }
    }

    private void handleUpdateSelected() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Select a part in the table first.");
            return;
        }
        try {
            Part result = openPartDialog(selected);
            if (result == null) {
                return;
            }
            inventoryManager.updatePart(selected.getCode(), result);
            refreshInventoryTab();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the Update Part dialog: " + e.getMessage());
        }
    }

    private void handleDeleteSelected() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Select a part in the table first.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete part " + selected.getCode() + " (" + selected.getName() + ")?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                inventoryManager.deletePart(selected.getCode());
                refreshInventoryTab();
            }
        });
    }

    private void handleSaveThreshold() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Select a part in the table first.");
            return;
        }
        int newThreshold;
        try {
            newThreshold = Integer.parseInt(thresholdField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid threshold", "Threshold must be a whole number.");
            return;
        }
        if (newThreshold < 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid threshold", "Threshold cannot be negative.");
            return;
        }
        inventoryManager.setLowStockThreshold(selected.getCode(), newThreshold);
        refreshInventoryTab();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
