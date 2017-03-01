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

import de.hs.mannheim.modUro.model.ModuroModel;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.overview.ModuroModelOverview;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ModuroModelOverviewController controls ModelOverviewView.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModuroModelOverviewController {

    //Reference to ModuroModelOverview
    private ModuroModelOverview moduroModelOverview;

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

    public void init(ModuroModel moduroModel) {
        // Has to be initialized here, otherwise MainApp.scene is null:
        this.moduroModelOverview = new ModuroModelOverview(moduroModel);
        List<StatisticValues> l = new ArrayList(moduroModelOverview.getStatisticValues().values());
        l.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));
        metricData = FXCollections.observableArrayList(l);
        setLabel();
        createTableData();
    }

    /**
     * Sets Data to Label.
     */
    private void setLabel() {
        this.numberOfSimulations.setText(String.valueOf(moduroModelOverview.getNumberOfSimulations()));
        this.numberOfSimulationInSteadyState.setText((String.valueOf(moduroModelOverview.getNumberOfSteadyStateSimulation())));
        this.numberOfAbortedSimulation.setText((String.valueOf(moduroModelOverview.getNumberOfAbortedSimulations())));
        this.numberOfCompletedSimulations.setText((String.valueOf(moduroModelOverview.getNumberOfCompletedSimulations())));
    }

    /**
     * Creates Table Data.
     */
    private void createTableData() {

        TableColumn column1 = new TableColumn("Data Series");
        column1.setCellValueFactory(new PropertyValueFactory<StatisticValues, String>("name"));

        TableColumn column2 = new TableColumn("Mean");
        column2.setCellValueFactory(new PropertyValueFactory<StatisticValues, Double>("meanAsString"));

        TableColumn column3 = new TableColumn("Standard Deviation");
        column3.setCellValueFactory(new PropertyValueFactory<StatisticValues, Double>("stdDevAsString"));

        metricDataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        metricDataTable.getColumns().setAll(column1, column2, column3);
        metricDataTable.setItems(metricData);
        metricDataTable.setFixedCellSize(30);
        metricDataTable.prefHeightProperty().bind(Bindings.size(metricDataTable.getItems()).multiply(metricDataTable.getFixedCellSize()).add(35));
    }
}
