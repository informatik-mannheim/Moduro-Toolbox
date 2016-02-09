package de.hs.mannheim.modUro.controller.overview;

import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.overview.ModeltypeOverview;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.Map;

/**
 * ModeltypeOverviewController controls ModelOverviewView.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeOverviewController {

    //Reference to ModeltypeOverview
    private ModeltypeOverview modeltypeOverview;

    private ObservableList<StatisticValues> metricData;


    @FXML
    private TableView<StatisticValues> metricDataTable;
    @FXML
    private Label numberOfSimulations;
    @FXML
    private Label numberOfSimulationInSteadyState;
    @FXML
    private Label numberOfAbortedSimulation;
    @FXML
    private Label numberOfCompletedSimulations;

    public void init(ModelType modelType) {
        this.modeltypeOverview = new ModeltypeOverview(modelType);
        metricData = FXCollections.observableArrayList(modeltypeOverview.getStatisticValues());

        setLabel();
        createTableData();
    }

    /**
     * Sets Data to Label.
     */
    private void setLabel() {
        this.numberOfSimulations.setText(String.valueOf(modeltypeOverview.getNumberOfSimulations()));
        this.numberOfSimulationInSteadyState.setText((String.valueOf(modeltypeOverview.getNumberOfSteadyStateSimulation())));
        this.numberOfAbortedSimulation.setText((String.valueOf(modeltypeOverview.getNumberOfAbortedSimulations())));
        this.numberOfCompletedSimulations.setText((String.valueOf(modeltypeOverview.getNumberOfCompletedSimulations())));
    }

    /**
     * Creates Table Data.
     */
    private void createTableData() {

        TableColumn column1 = new TableColumn("MetricType");
        column1.setCellValueFactory(new PropertyValueFactory<StatisticValues, String>("metricTypeName"));

        TableColumn column2 = new TableColumn("Mean");
        column2.setCellValueFactory(new PropertyValueFactory<StatisticValues, Double>("mean"));

        TableColumn column3 = new TableColumn("Standard Deviation");
        column3.setCellValueFactory(new PropertyValueFactory<StatisticValues, Double>("stdDev"));

        metricDataTable.getColumns().setAll(column1, column2, column3);
        metricDataTable.setItems(metricData);
    }
}
