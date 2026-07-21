package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.CartItem;
import utils.CartManager;
import java.util.List;


public class CartTabController {

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> cartCodeColumn, cartNameColumn;
    @FXML private TableColumn<CartItem, Integer> cartQtyColumn;
    @FXML private TableColumn<CartItem, Double> cartLineTotalColumn;
    @FXML private Label subtotalLabel, bulkDiscountLabel, synergyDiscountLabel, grandTotalLabel;
    @FXML private Button removeFromCartBtn, checkoutBtn;


    private CartManager cart;

    // Called once by MainWindowController after loading, to hand over the CartManager
    public void setCartManager(CartManager cart) {
        this.cart = cart;

        cartCodeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPart().getCode()));
        cartNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPart().getName()));
        cartQtyColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        cartLineTotalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(
                data.getValue().getQuantity() * data.getValue().getPart().getPrice()).asObject());

        refreshCart();
    }

    // Public so MainWindowController can call this after adding an item from the Inventory tab
    public void refreshCart() {
        cartTable.setItems(FXCollections.observableArrayList(cart.getItems()));

        double subtotal = cart.calculateSubTotal();
        double bulkDiscount = cart.calculateBulkDiscount();
        double synergyDiscount = cart.calculateSynergyDiscount(subtotal - bulkDiscount);
        double total = cart.calculateTotal();

        subtotalLabel.setText("Rs " + String.format("%.2f", subtotal));
        bulkDiscountLabel.setText("Rs " + String.format("%.2f", bulkDiscount));
        synergyDiscountLabel.setText("Rs " + String.format("%.2f", synergyDiscount));
        grandTotalLabel.setText("Rs " + String.format("%.2f", total));
    }

    @FXML
    private void handleRemoveFromCart() {
        CartItem selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a cart item first.");
            alert.showAndWait();
            return;
        }
        cart.removeItem(selected.getPart().getCode());
        refreshCart();
    }

    @FXML
    private void handleCheckout() {
        boolean success = cart.processCheckout();

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checkout Complete");
            alert.setHeaderText(null);
            alert.setContentText("Transaction processed successfully.");
            alert.showAndWait();
            refreshCart();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Checkout Failed");
            alert.setHeaderText(null);
            alert.setContentText("Cart is empty or stock is insufficient.");
            alert.showAndWait();
        }
    }




}