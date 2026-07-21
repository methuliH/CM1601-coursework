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

    @FXML private Button refreshDealersBtn;
    @FXML private TableView<Dealer> dealersTable;
    @FXML private TableColumn<Dealer, String> dealerNameColumn, dealerPhoneColumn, dealerLocationColumn;
    @FXML private Button seeAllDealersBtn;

    private DealerManager dealerManager;

    public void setDealerManager(DealerManager dealerManager) {
        this.dealerManager = dealerManager;

        dealerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dealerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dealerLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        handleRefreshDealers(); // load 4 dealers immediately
    }

    @FXML
    private void handleRefreshDealers() {
        List<Dealer> randomFour = dealerManager.selectRandomDealer();
        dealersTable.setItems(FXCollections.observableArrayList(randomFour));
    }

    @FXML
    private void handleSeeAllDealers(){
        List<Dealer> allDealers = dealerManager.getAllDealers();
        dealersTable.setItems(FXCollections.observableArrayList(allDealers));
    }
}