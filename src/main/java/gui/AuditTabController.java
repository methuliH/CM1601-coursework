package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.AuditLogEntry;
import utils.AuditLogger;

import java.util.ArrayList;
import java.util.List;

public class AuditTabController {
    @FXML private Button refreshAuditBtn;

    @FXML private TableView<AuditLogEntry> auditTable;
    @FXML private TableColumn<AuditLogEntry, String> timestampColumn;
    @FXML private TableColumn<AuditLogEntry, String> actionColumn;
    @FXML private TableColumn<AuditLogEntry, String> partCodeColumn;
    @FXML private TableColumn<AuditLogEntry, Integer> quantityColumn;

    @FXML
    public void initialize() {
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        partCodeColumn.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Colour each action using the tag-add/tag-update/tag-delete/tag-checkout
        // classes already reserved for this screen in app.css.
        actionColumn.setCellFactory(column -> new TableCell<AuditLogEntry, String>() {
            @Override
            protected void updateItem(String action, boolean empty) {
                super.updateItem(action, empty);
                getStyleClass().removeAll("tag-add", "tag-update", "tag-delete", "tag-checkout");

                if (empty || action == null) {
                    setText(null);
                    return;
                }

                setText(action);
                switch (action) {
                    case "ADD":
                        getStyleClass().add("tag-add");
                        break;
                    case "UPDATE":
                        getStyleClass().add("tag-update");
                        break;
                    case "DELETE":
                        getStyleClass().add("tag-delete");
                        break;
                    case "CHECKOUT":
                        getStyleClass().add("tag-checkout");
                        break;
                }
            }
        });

        refreshAuditBtn.setOnAction(e -> refreshAuditLog());
        refreshAuditLog();
    }

    // Shows the newest entries first, same convention as the Dashboard's recent-activity panel.
    public void refreshAuditLog() {
        List<AuditLogEntry> entries = AuditLogger.readAllEntries();
        List<AuditLogEntry> newestFirst = new ArrayList<>();
        for (int i = entries.size() - 1; i >= 0; i--) {
            newestFirst.add(entries.get(i));
        }
        auditTable.setItems(FXCollections.observableArrayList(newestFirst));
    }
}
