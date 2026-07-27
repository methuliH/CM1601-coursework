package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.CartItem;
import utils.CartManager;
import utils.InventoryManager;

public class CartTabController {
    @FXML private TextField cartPartCodeField;
    @FXML private TextField cartQuantityField;
    @FXML private Button addToCartBtn;

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> cartCodeColumn;
    @FXML private TableColumn<CartItem, String> cartNameColumn;
    @FXML private TableColumn<CartItem, String> cartCategoryColumn;
    @FXML private TableColumn<CartItem, String> unitPriceColumn;
    @FXML private TableColumn<CartItem, Integer> cartQuantityColumn;
    @FXML private TableColumn<CartItem, String> lineTotalColumn;

    @FXML private Button removeFromCartBtn;

    @FXML private Label subtotalLabel;
    @FXML private Label bulkDiscountLabel;
    @FXML private Label synergyDiscountLabel;
    @FXML private Label totalLabel;

    @FXML private Button checkoutBtn;

    private CartManager cartManager;

    public void setInventoryManager(InventoryManager inventoryManager){
        this.cartManager = new CartManager(inventoryManager);

    }

    private void setupCartTable() {
        cartCodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPart().getCode()));
        cartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPart().getName()));
        cartCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPart().getCategory()));
        unitPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("Rs %.2f", cellData.getValue().getPart().getPrice())));
        cartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        lineTotalColumn.setCellValueFactory(cellData -> {
            CartItem item = cellData.getValue();
            double lineTotal = item.getPart().getPrice() * item.getQuantity();
            return new SimpleStringProperty(String.format("Rs %.2f", lineTotal));
        });
    }

    @FXML public void initialize(){
        setupCartTable();
        addToCartBtn.setOnAction(e -> handleAddToCart());
        removeFromCartBtn.setOnAction(e -> handleRemoveSelected());
        checkoutBtn.setOnAction(e -> handleCheckout());
    }

    public void refreshCart() {
        cartTable.setItems(FXCollections.observableArrayList(cartManager.getItems()));

        double subtotal = cartManager.calculateSubTotal();
        double bulkDiscount = cartManager.calculateBulkDiscount();
        double synergyDiscount = cartManager.calculateSynergyDiscount(subtotal - bulkDiscount);
        double total = cartManager.calculateTotal();

        subtotalLabel.setText(String.format("Rs %.2f", subtotal));
        bulkDiscountLabel.setText(String.format("- Rs %.2f", bulkDiscount));
        synergyDiscountLabel.setText(String.format("- Rs %.2f", synergyDiscount));
        totalLabel.setText(String.format("Rs %.2f", total));
    }

    public void handleAddToCart(){
        String code = cartPartCodeField.getText().trim();
        if (code.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing code", "Enter a part code.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(cartQuantityField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid quantity", "Quantity must be a whole number.");
            return;
        }
        if (quantity <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid quantity", "Quantity must be greater than 0.");
            return;
        }

        boolean added = cartManager.addItem(code, quantity);
        if (!added) {
            showAlert(Alert.AlertType.ERROR, "Could not add item", "Check the part code exists and there's enough stock.");
            return;
        }

        cartPartCodeField.clear();
        cartQuantityField.clear();
        refreshCart();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    public void handleRemoveSelected(){
        CartItem selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Select an item in the cart first.");
            return;
        }

        boolean removed = cartManager.removeItem(selected.getPart().getCode());
        if (!removed) {
            showAlert(Alert.AlertType.ERROR, "Could not remove item", "That item is no longer in the cart.");
            return;
        }
        refreshCart();
    }

    public void handleCheckout(){
        if (cartManager.getItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cart is empty", "Add at least one item before checking out.");
            return;
        }

        boolean success = cartManager.processCheckout();
        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Checkout failed", "One or more items no longer have enough stock.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Checkout complete", "Transaction processed successfully.");
        refreshCart();
    }
}
