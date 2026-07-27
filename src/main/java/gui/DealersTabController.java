package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Dealer;
import utils.DealerManager;

import java.util.List;

public class DealersTabController {
    @FXML private Button pickDealersBtn;

    @FXML private TableView<Dealer> selectedDealersTable;
    @FXML private TableColumn<Dealer, String> selectedIdColumn;
    @FXML private TableColumn<Dealer, String> selectedNameColumn;
    @FXML private TableColumn<Dealer, String> selectedPhoneColumn;
    @FXML private TableColumn<Dealer, String> selectedLocationColumn;

    @FXML private TableView<Dealer> allDealersTable;
    @FXML private TableColumn<Dealer, String> allIdColumn;
    @FXML private TableColumn<Dealer, String> allNameColumn;
    @FXML private TableColumn<Dealer, String> allPhoneColumn;
    @FXML private TableColumn<Dealer, String> allLocationColumn;

    private DealerManager dealerManager;

    public void setDealers(List<Dealer> dealers) {
        this.dealerManager = new DealerManager(dealers);
        allDealersTable.setItems(FXCollections.observableArrayList(dealerManager.getAllDealers()));
    }

    @FXML
    public void initialize() {
        selectedIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        selectedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        selectedPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        selectedLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        allIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        allLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        pickDealersBtn.setOnAction(e -> handlePickDealers());
    }

    private void handlePickDealers() {
        List<Dealer> selected = dealerManager.selectRandomDealer();
        selectedDealersTable.setItems(FXCollections.observableArrayList(selected));
    }
}
