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
package de.hs.mannheim.modUro.controller.diagram;

import de.hs.mannheim.modUro.config.FitnessName;
import de.hs.mannheim.modUro.controller.diagram.fx.ChartViewer;
import de.hs.mannheim.modUro.model.ModuroModel;
import de.hs.mannheim.modUro.model.TimeSeries;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.diagram.ModuroModelDiagram;
import de.hs.mannheim.modUro.diagram.Diagram;
import de.hs.mannheim.modUro.reader.JTimeSeriesDiagram;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

/**
 * ModuroModelDiagramController controls ModelDiagramView.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModuroModelDiagramController {

    //Reference to ModelDiagram
    private ModuroModelDiagram moduroModelDiagram;

    @FXML
    private BorderPane leftPane;
    @FXML
    private BorderPane rightPane;
    @FXML
    private ChoiceBox leftMetricType;
    @FXML
    private ChoiceBox rightMetricType;

    private static Integer leftLastSelectedIndex;
    private static Integer rightLastSelectedIndex;

    private static String leftLastSelectedMetrictypename;
    private static String rightLastSelectedMetrictypename;


    public void init(ModuroModel moduroModel) {
        this.moduroModelDiagram = new ModuroModelDiagram(moduroModel);
        if (moduroModel.getSimulations().isEmpty()) {
            // No simulations to show!
            System.err.println("leer");
            return;
        }

        if (leftLastSelectedIndex == null || rightLastSelectedIndex == null) {
            initializeChoiceboxContent();
        } else {
            if (simulationContainsMetricType()) {
                setChoiceBoxContent();
                setChartContent(moduroModelDiagram.getMetricTypeNames().get(leftLastSelectedIndex), true);
                setChartContent(moduroModelDiagram.getMetricTypeNames().get(rightLastSelectedIndex), false);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Metrictype Warning");
                alert.setContentText("Simulation does not have Metrictype: " + leftLastSelectedMetrictypename);
                alert.showAndWait();

                initializeChoiceboxContent();
            }
        }

        /*ChangeListerners for selected items in choicebox.*/
        leftMetricType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setChartContent(moduroModelDiagram.getMetricTypeNames().get(newValue.intValue()), true);
                leftLastSelectedIndex = newValue.intValue();
                leftLastSelectedMetrictypename = moduroModelDiagram.getMetricTypeNames().get(leftLastSelectedIndex);

            }
        });

        rightMetricType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setChartContent(moduroModelDiagram.getMetricTypeNames().get(newValue.intValue()), false);
                rightLastSelectedIndex = newValue.intValue();
                rightLastSelectedMetrictypename = moduroModelDiagram.getMetricTypeNames().get(rightLastSelectedIndex);
            }
        });
    }

    /**
     * Checks if simultion has the last selected Metrictype from another simultion.
     *
     * @return
     */
    private boolean simulationContainsMetricType() {
        boolean containsMetricType = false;
        List<String> name = moduroModelDiagram.getMetricTypeNames();

        if (name.contains(leftLastSelectedMetrictypename) && name.contains(rightLastSelectedMetrictypename)) {
            containsMetricType = true;
        }

        return containsMetricType;
    }

    /**
     * Initializes Choicebox Content.
     */
    private void initializeChoiceboxContent() {
        List<String> name = moduroModelDiagram.getMetricTypeNames();

        int left = 0;
        int right = 0;

        for (String val : name) {
            if (val.equals(FitnessName.ARRANGEMENT_FITNESS.getName())) {
                left = name.indexOf(FitnessName.ARRANGEMENT_FITNESS.getName());
            }

            if (val.equals(FitnessName.VOLUME_FITNESS.getName())) {
                right = name.indexOf(FitnessName.VOLUME_FITNESS.getName());
            }
        }

        leftMetricType.setItems(FXCollections.observableArrayList(name));
        rightMetricType.setItems(FXCollections.observableArrayList(name));

        leftMetricType.getSelectionModel().select(left);
        rightMetricType.getSelectionModel().select(right);

        leftLastSelectedIndex = left;
        rightLastSelectedIndex = right;

        leftLastSelectedMetrictypename = name.get(leftLastSelectedIndex);
        rightLastSelectedMetrictypename = name.get(rightLastSelectedIndex);

        setChartContent(name.get(left), true);
        setChartContent(name.get(right), false);
    }

    /**
     * Sets Content of Choicebox.
     */
    private void setChoiceBoxContent() {
        List<String> name = moduroModelDiagram.getMetricTypeNames();

        leftMetricType.setItems(FXCollections.observableArrayList(name));
        rightMetricType.setItems(FXCollections.observableArrayList(name));

        leftMetricType.getSelectionModel().select(leftLastSelectedIndex.intValue());
        rightMetricType.getSelectionModel().select(rightLastSelectedIndex.intValue());
    }

    /**
     * Sets right Chartcontent.
     *
     * @param selectedItem
     */
    private void setChartContent(String selectedItem, boolean left) {

        List<TimeSeries> list = new ArrayList<>();
        for (Simulation simulation : moduroModelDiagram.getSimulationList()) {
            TimeSeries metricTypeItem = simulation.getTimeSeriesByName(selectedItem);
            list.add(metricTypeItem);
        }
        Diagram diagram = new JTimeSeriesDiagram(list.get(0).getName(), list);
        diagram.getJFreeChart().removeLegend();
        ChartViewer viewer = new ChartViewer(diagram);
        if (left) {
            leftPane.setCenter(viewer);
            leftPane.layout();
        } else {
            rightPane.setCenter(viewer);
            rightPane.layout();
        }
    }
}
