package de.hs.mannheim.modUro.controller.overview;

import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.overview.SimulationOverview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * SimulationOverviewController controls SimulationOverviewView.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationOverviewController {

    //Reference to ProjectOverview
    private SimulationOverview simulationOverview;

    @FXML
    private Label simulationID;
    @FXML
    private Label simulationName;
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
    private Label metricType;
    @FXML
    private TableView tableContent;
    @FXML
    private ImageView firstImage;
    @FXML
    private ImageView secondImage;
    @FXML
    private ImageView thirdImage;


    private ObservableList<MetricType> metricData;


    public void init(Simulation simulation){
        this.simulationOverview = new SimulationOverview(simulation);
        metricData = FXCollections.observableArrayList(simulationOverview.getMetricTypes());

        setLabel();
        setImage();
        createTableContent();
    }

    /**
     * Sets Simulation Details to Label
     */
    private void setLabel() {
        this.simulationID.setText(String.valueOf(simulationOverview.getSimulationID()));
        this.simulationName.setText(String.valueOf(simulationOverview.getSimulationName()));
        this.simulationModel.setText(simulationOverview.getModelType());
        this.isCompleted.setText(String.valueOf(simulationOverview.isCompleted()));
        this.isAborted.setText(String.valueOf(simulationOverview.isAborted()));
        this.isInSteadyState.setText(String.valueOf(simulationOverview.isInSteadyState()));
        this.starttime.setText(simulationOverview.getStartTime().toString());
        this.duration.setText(String.valueOf(simulationOverview.getDuration()));
        this.directoryHyperlink.setText(simulationOverview.getDirectory().getAbsolutePath());
        this.metricType.setText(String.valueOf(simulationOverview.getMetricTypesName()));
    }

    private void setImage() {
        List<File> imageFiles = simulationOverview.getImages();

        Image image = new Image(imageFiles.get(0).toURI().toString());
        firstImage.setImage(image);

        Image image2 = new Image(imageFiles.get(1).toURI().toString());
        secondImage.setImage(image2);

        Image image3 = new Image(imageFiles.get(2).toURI().toString());
        thirdImage.setImage(image3);
    }

    /**
     * Creates table content.
     */
    private void createTableContent() {
        TableColumn column1 = new TableColumn("MetricType");
        column1.setCellValueFactory(new PropertyValueFactory<MetricType, String>("name"));

        TableColumn column2 = new TableColumn("Mean");
        column2.setCellValueFactory(new PropertyValueFactory<MetricType, Double>("mean"));

        TableColumn column3 = new TableColumn("Standard Deviation");
        column3.setCellValueFactory(new PropertyValueFactory<MetricType, Double>("deviation"));

        tableContent.getColumns().setAll(column1, column2, column3);
        tableContent.setItems(metricData);
    }


    /**
     * Handel HyperlinkClicked Event.
     * @param actionEvent
     */
    public void handleHyperlinkOnlicked(ActionEvent actionEvent) {
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen = null;
        try {
            dirToOpen = new File(directoryHyperlink.getText());
            desktop.open(dirToOpen);
        } catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
