package main;

import gui.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.FileParser;
import utils.PartParser;
import utils.DealerParser;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FileParser.initializeData();
        var parts = PartParser.InventoryFileIntoObj();
        System.out.println("Parsed parts: " + parts.size());
        var dealers = DealerParser.dealerFileIntoObj();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"));
        Parent root = loader.load();

        MainWindowController controller = loader.getController();
        controller.initialize(parts, dealers);

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setTitle("Malabe Spares Depot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}