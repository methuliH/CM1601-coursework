package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.util.List;
import utils.AuditLogger;
import utils.InventoryManager;
import models.AuditLogEntry;
import models.Dealer;
import models.Part;

public class MainWindowController {

    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button cartBtn;
    @FXML private Button dealersBtn;
    @FXML private Button auditBtn;

    @FXML private StackPane contentArea;
    @FXML private VBox dashboardTab;
    @FXML private VBox inventoryTab;
    @FXML private VBox cartTab;
    @FXML private VBox dealersTab;
    @FXML private VBox auditTab;

    @FXML private Button addPartBtn;

    @FXML private Label totalItemsLabel;
    @FXML private Label totalValueLabel;
    @FXML private Label lowStockLabel;

    @FXML private HBox lowStockListBox;
    @FXML private VBox recentActivityBox;
    @FXML private VBox dealerPreviewBox;

    private InventoryManager inventoryManager;
    private List<Dealer> dealers;

    public void initialize(List<Part> parts, List<Dealer> dealers) {
        this.inventoryManager = new InventoryManager(parts);
        this.dealers = dealers;
        wireSidebar();
        refreshDashboard();
    }

    private void wireSidebar() {
        dashboardBtn.setOnAction(e -> showTab(dashboardTab, dashboardBtn));
        inventoryBtn.setOnAction(e -> showTab(inventoryTab, inventoryBtn));
        cartBtn.setOnAction(e -> showTab(cartTab, cartBtn));
        dealersBtn.setOnAction(e -> showTab(dealersTab, dealersBtn));
        auditBtn.setOnAction(e -> showTab(auditTab, auditBtn));
    }

    private void showTab(VBox tab, Button activeBtn) {
        dashboardTab.setVisible(false);
        inventoryTab.setVisible(false);
        cartTab.setVisible(false);
        dealersTab.setVisible(false);
        auditTab.setVisible(false);
        tab.setVisible(true);

        for (Button btn : new Button[]{dashboardBtn, inventoryBtn, cartBtn, dealersBtn, auditBtn}) {
            btn.getStyleClass().remove("nav-btn-active");
        }
        activeBtn.getStyleClass().add("nav-btn-active");
    }

    private void refreshDashboard() {
        totalItemsLabel.setText("Total Items: " + inventoryManager.getTotalItemCount());
        totalValueLabel.setText(String.format("Total Value: Rs %.2f", inventoryManager.getTotalInventoryValue()));

        List<Part> lowStock = inventoryManager.getLowStockItems();
        lowStockLabel.setText("Low Stock: " + lowStock.size());

        populateLowStockPanel(lowStock);
        populateRecentActivity();
        populateDealerPreview();
    }

    private void populateLowStockPanel(List<Part> lowStockParts) {
        lowStockListBox.getChildren().clear();
        for (Part p : lowStockParts) {
            Label name = new Label(p.getName() + " (" + p.getCode() + ")");
            name.getStyleClass().add("alert-item-name");
            Label badge = new Label("Qty: " + p.getQty());
            badge.getStyleClass().add("alert-item-badge");
            HBox row = new HBox(8, name, badge);
            row.getStyleClass().add("alert-item");
            lowStockListBox.getChildren().add(row);
        }
    }

    private void populateRecentActivity() {
        recentActivityBox.getChildren().clear();
        List<AuditLogEntry> entries = AuditLogger.readAllEntries();
        int start = Math.max(0, entries.size() - 5);
        for (int i = entries.size() - 1; i >= start; i--) {
            AuditLogEntry e = entries.get(i);
            Label row = new Label(e.getTimestamp() + "  •  " + e.getAction() + "  " + e.getPartCode());
            row.getStyleClass().add("activity-row");
            recentActivityBox.getChildren().add(row);
        }
    }

    private void populateDealerPreview() {
        dealerPreviewBox.getChildren().clear();
        int limit = Math.min(dealers.size(), 5);
        for (int i = 0; i < limit; i++) {
            Dealer d = dealers.get(i);
            Label row = new Label(d.getName() + "  —  " + d.getLocation());
            row.getStyleClass().add("dealer-row");
            dealerPreviewBox.getChildren().add(row);
        }
    }
}
