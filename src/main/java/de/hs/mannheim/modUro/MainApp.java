/*
Copyright 2016 the original author or authors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package de.hs.mannheim.modUro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * Class of the MainApp. Application starts from here.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class MainApp extends Application {

    // Main window of the application.
    private Stage primaryStage;
    private BorderPane mainLayout;
    public static String VERSION = "UNKNOWN";

    @Override
    public void start(Stage primaryStage) {
        String version = "UNKNOWN";
        try (InputStream versionInput = Optional.ofNullable(MainApp.class.getClassLoader().
                getResourceAsStream("version.properties")).orElseThrow(IOException::new)) {
            Properties versionProperties = new Properties();
            versionProperties.load(versionInput);
            version = versionProperties.getProperty("version", version);
        } catch (IOException e1) {
            try (InputStream mavenInput = Optional.ofNullable(MainApp.class.getClassLoader().
                    getResourceAsStream("META-INF/maven/pom.properties")).orElseThrow(IOException::new)) {
                Properties mavenProperties = new Properties();
                mavenProperties.load(mavenInput);
                version = mavenProperties.getProperty("version", version);
            } catch (IOException e2) { /* nothing to do here */ }
        } finally {
            VERSION = version;
        }
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ModUro Toolbox");
        initRootLayout();
    }

    /**
     * Initializes the root Layout.
     */
    private void initRootLayout() {
        try {
            //Load main layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/FXML/MainLayout.fxml"));
            mainLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(650);
            primaryStage.setMinWidth(970);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
