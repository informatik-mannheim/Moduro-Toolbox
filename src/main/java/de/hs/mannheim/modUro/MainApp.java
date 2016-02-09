package de.hs.mannheim.modUro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Class of the MainApp. Application starts from here.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class MainApp extends Application {

    // Main window of the application.
    private Stage primaryStage;
    private BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ModUro Toolbox");
        initRootLayout();
    }

    /**
     * Initializes the root Layout.
     */
    private void initRootLayout() {
        try{
            //Load main layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/MainLayout.fxml"));
            mainLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(650);
            primaryStage.setMinWidth(970);
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}
