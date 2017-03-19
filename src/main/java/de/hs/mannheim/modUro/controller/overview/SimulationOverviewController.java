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
package de.hs.mannheim.modUro.controller.overview;

import de.hs.mannheim.modUro.config.ToolboxLogger;
import de.hs.mannheim.modUro.model.TimeSeries;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.overview.SimulationOverview;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * SimulationOverviewController controls SimulationOverviewView.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationOverviewController {

    //Reference to ProjectOverview
    private SimulationOverview simulationOverview;

    @FXML
    private Label simulationID;
    @FXML
    private Label simulationSeed;
    @FXML
    private Label simulationModel;
    @FXML
    private Label isCompleted;
    @FXML
    private Label isAborted;
    @FXML
    private Label isInSteadyState;
    @FXML
    private Label starttime;
    @FXML
    private Label duration;
    @FXML
    private Hyperlink directoryHyperlink;
    @FXML
    private Button videoButton;
    @FXML
    private TableView tableContent;
    @FXML
    private ImageView firstImage;
    @FXML
    private ImageView secondImage;
    @FXML
    private ImageView thirdImage;

    public void init(Simulation simulation) {
        this.simulationOverview = new SimulationOverview(simulation);
        setLabel();
        setImage();
        createTableContent();
    }

    /**
     * Sets Simulation Details to Label
     */
    private void setLabel() {
        this.simulationID.setText(String.valueOf(simulationOverview.getSimulationID()));
        this.simulationModel.setText(simulationOverview.getModelType());
        this.simulationSeed.setText(simulationOverview.getSeed());
        this.isCompleted.setText(String.valueOf(simulationOverview.isCompleted()));
        this.isAborted.setText(String.valueOf(simulationOverview.isAborted()));
        this.isInSteadyState.setText(String.valueOf(simulationOverview.isInSteadyState()));
        this.starttime.setText(simulationOverview.getStartTime().toString());
        this.duration.setText(String.valueOf(simulationOverview.getDuration()));
        this.directoryHyperlink.setText(simulationOverview.getDirectory().getAbsolutePath());
        if (simulationOverview.getSimulation().hasVideo()) {
            videoButton.setText("Open Video");
        } else {
            videoButton.setText("Create Video");
        }
    }

    /**
     * Set Imagepath to JavaFX Image-element
     */
    private void setImage() {
        List<File> imageFiles = simulationOverview.getImages();

        if (imageFiles.size() != 0) {
            Image image = new Image(imageFiles.get(0).toURI().toString());
            firstImage.setImage(image);

            Image image2 = new Image(imageFiles.get(1).toURI().toString());
            secondImage.setImage(image2);

            Image image3 = new Image(imageFiles.get(2).toURI().toString());
            thirdImage.setImage(image3);
        }

    }

    /**
     * Creates table content.
     */
    private void createTableContent() {
        ObservableList<TimeSeries> metricData =
                FXCollections.observableArrayList(simulationOverview.getMetricTypes());

        TableColumn column1 = new TableColumn("Data Series");
        column1.setCellValueFactory(new PropertyValueFactory<TimeSeries, String>("name"));
        //column1.setMinWidth(300.0);

        TableColumn column2 = new TableColumn("Mean");
        column2.setCellValueFactory(new PropertyValueFactory<TimeSeries, String>("meanAsString"));
        //column2.setMinWidth(200.0);

        TableColumn column3 = new TableColumn("Standard Deviation");
        column3.setCellValueFactory(new PropertyValueFactory<TimeSeries, String>("stdDevAsString"));
        //column3.setMinWidth(200.0);

        tableContent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableContent.getColumns().clear();
        tableContent.getColumns().addAll(column1, column2, column3);
        tableContent.setItems(metricData);
        // http://stackoverflow.com/questions/27945817/javafx-adapt-tableview-height-to-number-of-rows
        tableContent.setFixedCellSize(30);
        tableContent.prefHeightProperty().bind(Bindings.size(tableContent.getItems()).multiply(tableContent.getFixedCellSize()).add(35));
    }

    /**
     * Handel HyperlinkClicked Event.
     *
     * @param actionEvent
     */
    public void handleHyperlinkOnlicked(ActionEvent actionEvent) {
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen = null;
        try {
            dirToOpen = new File(directoryHyperlink.getText());
            desktop.open(dirToOpen);
        } catch (IllegalArgumentException iae) {
            ToolboxLogger.log.warning("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ImagesToVideo
     *
     * @param actionEvent
     */
    public void handleImagesToVideo(ActionEvent actionEvent) {
        // Open or create video?
        if (simulationOverview.getSimulation().hasVideo()) {
            String pathToVideoViewer = "vlc.exe";
            String srcDir = simulationOverview.getDirectory().getAbsolutePath();
            String videoFile = srcDir + "\\video.wmv";
            try {
                Process process = new ProcessBuilder(pathToVideoViewer, videoFile).start();
            } catch (Exception e) {
                ToolboxLogger.log.warning("Cannot play video " +
                videoFile);
                ToolboxLogger.log.warning("Invocation: " +
                        pathToVideoViewer + " " + videoFile);
            }
        } else {
            try {
                String pathToImagesToVideoExe = "ImagesToVideo.exe";

                String srcDir = simulationOverview.getDirectory().getAbsolutePath();
                String destFile = simulationOverview.getDirectory().getAbsolutePath() + "\\video";
                String config = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<SVNDirectory xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                        "  <version>400</version>\n" +
                        "  <sourceDir>" + srcDir + " </sourceDir>\n" +
                        "  <destFile>" + destFile + "</destFile>\n" +
                        "  <useSubfolders>false</useSubfolders>\n" +
                        "  <sortBy>0</sortBy>\n" +
                        "  <kbitrate>-1</kbitrate>\n" +
                        "  <fps>15</fps>\n" +
                        "  <videocodec>1</videocodec>\n" +
                        "  <cropleft>0</cropleft>\n" +
                        "  <croptop>0</croptop>\n" +
                        "  <cropwidth>0</cropwidth>\n" +
                        "  <cropheight>0</cropheight>\n" +
                        "  <imageExtension>png</imageExtension>\n" +
                        "  <tempdir>C:\\temp\\</tempdir>\n" +
                        "  <regexp>.*</regexp>\n" +
                        "  <before>2008-11-07T00:00:00</before>\n" +
                        "  <after>2008-11-07T23:59:59</after>\n" +
                        "  <width>-1</width>\n" +
                        "  <height>-1</height>\n" +
                        "  <DeleteSourceFiles>false</DeleteSourceFiles>\n" +
                        "  <SoftwareScale>false</SoftwareScale>\n" +
                        "  <FlipHorizontal>false</FlipHorizontal>\n" +
                        "  <FlipVertical>false</FlipVertical>\n" +
                        "  <Rotate>0</Rotate>\n" +
                        "  <CPUThreads>2</CPUThreads>\n" +
                        "</SVNDirectory>";
                String configFile = "C:\\temp\\ImagesToVideo-Moduro.xml";
                PrintStream ps = new PrintStream(new File(configFile));
                ps.println(config);
                ps.close();

                Process process = new ProcessBuilder(pathToImagesToVideoExe, "--config", configFile).start();
                simulationOverview.getSimulation().refresh();
                videoButton.setText("Open Video");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
