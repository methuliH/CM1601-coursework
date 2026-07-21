package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import models.Part;
import models.Dealer;
import models.AuditLogEntry;
import utils.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainWindowController {

    @FXML private Button inventoryBtn, cartBtn, dealersBtn;
    @FXML private StackPane contentArea;
    @FXML private VBox inventoryTab, cartTab, dealersTab;
    @FXML private Label totalItemsLabel, totalValueLabel, lowStockLabel;
    @FXML private Button addPartBtn;
    @FXML private TableView<Part> inventoryTable;
    @FXML private javafx.scene.control.TableColumn<Part, String> codeColumn;
    @FXML private javafx.scene.control.TableColumn<Part, String> nameColumn;
    @FXML private javafx.scene.control.TableColumn<Part, String> brandColumn;
    @FXML private javafx.scene.control.TableColumn<Part, String> categoryColumn;
    @FXML private javafx.scene.control.TableColumn<Part, Double> priceColumn;
    @FXML private javafx.scene.control.TableColumn<Part, Integer> quantityColumn;
    @FXML private javafx.scene.control.TableColumn<Part, String> dateAddedColumn;
    @FXML private Button auditBtn, updateSelectedBtn, deleteSelectedBtn, saveThresholdBtn;
    @FXML private VBox auditTab, lowStockListBox;
    @FXML private TextField lowStockThresholdField;
    @FXML private TableView<AuditLogEntry> auditTable;
    @FXML private TableColumn<AuditLogEntry, String> auditTimestampColumn, auditActionColumn, auditCodeColumn;
    @FXML private TableColumn<AuditLogEntry, Integer> auditQuantityColumn;
    @FXML private javafx.scene.control.ComboBox<String> searchCategoryCombo;
    @FXML private TextField searchKeywordField, minPriceField, maxPriceField;
    @FXML private DealersTabController dealersTabController;
    @FXML private CartTabController cartTabController;


    private InventoryManager inventory;
    private DealerManager dealerManager;
    private CartManager cart;


    public void initialize(List<Part> parts, List<Dealer> dealers) {
        this.inventory = new InventoryManager(parts);
        this.dealerManager = new DealerManager(dealers);
        this.cart = new CartManager(inventory);

        inventoryBtn.setOnAction(e -> showTab(inventoryTab));
        cartBtn.setOnAction(e -> showTab(cartTab));
        dealersBtn.setOnAction(e -> showTab(dealersTab));
        auditBtn.setOnAction(e -> showTab(auditTab));

        dealersTabController.setDealerManager(dealerManager);
        cartTabController.setCartManager(cart);

        populateInventoryTab();
        populateCategoryDropdown();
        populateAuditTab();
        showTab(inventoryTab);
    }

    private void populateInventoryTab() {
        List<Part> allParts = inventory.getAllPartsSortedByCategory();
        int totalCount = inventory.getTotalItemCount();
        double totalValue = inventory.getTotalInventoryValue();
        List<Part> lowStockItems = inventory.getLowStockItems();

        totalItemsLabel.setText("Total Items: " + totalCount);
        totalValueLabel.setText("Total Value: Rs " + String.format("%.2f", totalValue));
        lowStockLabel.setText("Low Stock: " + lowStockItems.size());

        codeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("brand"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("qty"));
        dateAddedColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateAdded"));

        inventoryTable.setItems(FXCollections.observableArrayList(allParts));
        populateLowStockPanel();
    }

    // category dropdown
    private void populateCategoryDropdown() {
        List<Part> allParts = inventory.getAllParts();
        List<String> categories = new ArrayList<>();

        for (int i = 0; i < allParts.size(); i++) {
            String category = allParts.get(i).getCategory();
            boolean exists = false;
            for (int j = 0; j < categories.size(); j++) {
                if (categories.get(j).equals(category)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                categories.add(category);
            }
        }

        searchCategoryCombo.getItems().add("All Categories");
        for (int i = 0; i < categories.size(); i++) {
            searchCategoryCombo.getItems().add(categories.get(i));
        }
        searchCategoryCombo.setValue("All Categories");
    }

    private void showTab(VBox tab) {
        inventoryTab.setVisible(false);
        cartTab.setVisible(false);
        dealersTab.setVisible(false);
        auditTab.setVisible(false);
        tab.setVisible(true);
    }

    @FXML
    private void handleAddPart() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/gui/AddPartDialog.fxml"));
            javafx.scene.Parent root = loader.load();

            AddPartDialogController dialogController = loader.getController();

            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Add New Part");
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setScene(new javafx.scene.Scene(root));
            dialogStage.showAndWait();

            if (dialogController.isSaved()) {
                boolean success = inventory.addPart(dialogController.getResultPart());
                if (success) {
                    populateInventoryTab();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Part Code");
                    alert.setHeaderText(null);
                    alert.setContentText("A part with code \"" + dialogController.getResultPart().getCode()
                            + "\" already exists. Please use a different code.");
                    alert.showAndWait();
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void handleExport() {}

    // SEARCH FUNCTION
    @FXML private void handleSearch() {
        String keyword = searchKeywordField.getText().trim();
        String category = searchCategoryCombo.getValue();
        if(category == null || category.equals("All Categories")){
            category = "";
        }
        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;

        try {
            if (!minPriceField.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            }
            if (!maxPriceField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Price");
            alert.setHeaderText(null);
            alert.setContentText("Min Price and Max Price must be valid numbers.");
            alert.showAndWait();
            return;
        }

        List<Part> results = inventory.search(keyword, category, minPrice, maxPrice);
        inventoryTable.setItems(FXCollections.observableArrayList(results));
    }


    @FXML private void handleClearSearch() {
        searchKeywordField.clear();
        minPriceField.clear();
        maxPriceField.clear();
        searchCategoryCombo.setValue("All Categories");
        populateInventoryTab();
    }
    @FXML private void handleUpdateSelected() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part in the table first.");
            alert.showAndWait();
            return;
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/gui/AddPartDialog.fxml"));
            javafx.scene.Parent root = loader.load();

            AddPartDialogController dialogController = loader.getController();
            dialogController.setPartToEdit(selected); // pre-fill the form

            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Update Part");
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setScene(new javafx.scene.Scene(root));
            dialogStage.showAndWait();

            if (dialogController.isSaved()) {
                inventory.updatePart(dialogController.getOriginalCode(), dialogController.getResultPart());
                populateInventoryTab();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void handleDeleteSelected() {Part selected = inventoryTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part in the table first.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete part \"" + selected.getCode() + " - " + selected.getName() + "\"?");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            inventory.deletePart(selected.getCode());
            populateInventoryTab();
        }}




    private void populateLowStockPanel() {
        lowStockListBox.getChildren().clear();
        List<Part> lowStockItems = inventory.getLowStockItems();

        if (lowStockItems.isEmpty()) {
            Label none = new Label("No low stock items");
            lowStockListBox.getChildren().add(none);
        } else {
            for (int i = 0; i < lowStockItems.size(); i++) {
                Part p = lowStockItems.get(i);
                Label item = new Label(p.getCode() + " - " + p.getName() + " (qty: " + p.getQty() + ")");
                item.getStyleClass().add("low-stock-item");
                lowStockListBox.getChildren().add(item);
            }
        }
    }
    @FXML private void handleSaveThreshold() {try {
        int newThreshold = Integer.parseInt(lowStockThresholdField.getText().trim());

        if (newThreshold < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Threshold");
            alert.setHeaderText(null);
            alert.setContentText("Threshold cannot be negative.");
            alert.showAndWait();
            return;
        }

        inventory.setLowStockThreshold(newThreshold);
        lowStockThresholdField.setText(String.valueOf(inventory.getLowStockThreshold()));
        populateInventoryTab();
    } catch (NumberFormatException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Threshold");
        alert.setHeaderText(null);
        alert.setContentText("Threshold must be a whole number.");
        alert.showAndWait();
    }}
    @FXML private void handleAddToCart() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();

        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part in the table first");
            alert.showAndWait();
            return;
        }

        TextInputDialog qtyDialog = new TextInputDialog("1");
        qtyDialog.setTitle("Add to Cart");
        qtyDialog.setHeaderText("Add \"" + selected.getName()+"\" to cart");
        qtyDialog.setContentText("Quantity: ");

        Optional<String> result = qtyDialog.showAndWait();

        result.ifPresent(input -> {
            try {
                int quantity = Integer.parseInt(input.trim());
                if (quantity <= 0) {
                    showCartError("Quantity must be greater than 0.");
                    return;
                }
                if (quantity > selected.getQty()) {
                    showCartError("Only " + selected.getQty() + " in stock.");
                    return;
                }
                cart.addItem(selected.getCode(), quantity);
                cartTabController.refreshCart();
            } catch (NumberFormatException e) {
                showCartError("Quantity must be a whole number.");
            }
        });
    }

    private void populateAuditTab() {
        auditTimestampColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("timestamp"));
        auditActionColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("action"));
        auditCodeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("partCode"));
        auditQuantityColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantity"));

        List<AuditLogEntry> entries = AuditLogger.readAllEntries();
        auditTable.setItems(FXCollections.observableArrayList(entries));
    }

    private void showCartError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Quantity");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}