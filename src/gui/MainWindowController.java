package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import models.Part;
import models.Dealer;
import utils.*;

import java.util.List;

public class MainWindowController {

    @FXML
    private Button inventoryBtn, searchBtn, cartBtn, dealersBtn;
    @FXML
    private StackPane contentArea;
    @FXML
    private VBox inventoryTab, searchTab, cartTab, dealersTab;

    // Inventory tab components
    @FXML
    private Label totalItemsLabel, totalValueLabel, lowStockLabel;
    @FXML
    private Button addPartBtn, exportBtn;
    @FXML
    private TableView<Part> inventoryTable;


    @FXML
    private javafx.scene.control.TableColumn<Part, String> codeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Part, String> nameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Part, String> brandColumn;
    @FXML
    private javafx.scene.control.TableColumn<Part, String> categoryColumn;
    @FXML
    private javafx.scene.control.TableColumn<Part, Double> priceColumn;
    @FXML
    private javafx.scene.control.TableColumn<Part, Integer> quantityColumn;
    @FXML
    private javafx.scene.control.TableColumn<Part, String> dateAddedColumn;


    private InventoryManager inventory;
    private DealerManager dealerManager;
    private CartManager cart;

    public void initialize(List<Part> parts, List<Dealer> dealers) {
        this.inventory = new InventoryManager(parts);
        this.dealerManager = new DealerManager(dealers);
        this.cart = new CartManager(inventory);

        // Set up tab switching
        inventoryBtn.setOnAction(e -> showTab(inventoryTab));
        searchBtn.setOnAction(e -> showTab(searchTab));
        cartBtn.setOnAction(e -> showTab(cartTab));
        dealersBtn.setOnAction(e -> showTab(dealersTab));

        // Populate inventory tab
        populateInventoryTab();

        // Show inventory tab by default
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

        //columns in the inventory table
        codeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("brand"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("qty"));
        dateAddedColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateAdded"));

        inventoryTable.setItems(FXCollections.observableArrayList(allParts));
    }

    private void showTab(VBox tab) {
        inventoryTab.setVisible(false);
        searchTab.setVisible(false);
        cartTab.setVisible(false);
        dealersTab.setVisible(false);

        tab.setVisible(true);
    }

    @FXML
    private void handleAddPart() {
        // TODO: open add-part dialog
    }

    @FXML
    private void handleExport() {

    }

    @FXML
    private void handleSearch() {

    }

    @FXML
    private void handleClearSearch() {

    }

    @FXML
    private void handleRemoveFromCart() {

    }

    @FXML
    private void handleCheckout() {

    }

    @FXML
    private void handleRefreshDealers() {

    }
}