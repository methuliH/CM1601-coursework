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


}
