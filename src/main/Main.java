package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.FileParser;
import utils.PartParser;
import utils.DealerParser;
import models.Part;
import models.Dealer;
import java.util.List;

public class Main extends Application {

    // Store data as instance variables so GUI can access them
    private List<Part> parts;
    private List<Dealer> dealers;

    @Override
    public void start(Stage primaryStage) {
        // Step 1: Initialize files (clean legacy data)
        FileParser.initializeData();
        System.out.println("Files initialized");

        // Step 2: Load data into objects
        parts = PartParser.InventoryFileIntoObj();
        dealers = DealerParser.dealerFileIntoObj();

        System.out.println("Loaded " + parts.size() + " parts");
        System.out.println("Loaded " + dealers.size() + " dealers");

        // TODO: Build GUI using parts and dealers data

        // GUI stuff (temporary)
        Label label = new Label("Malabe Spares Depot\nParts: " + parts.size() + " | Dealers: " + dealers.size());
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Malabe Spares Depot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}