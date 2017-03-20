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
import de.hs.mannheim.modUro.diagram.Diagram;
import de.hs.mannheim.modUro.diagram.JCellCountDiagram;
import de.hs.mannheim.modUro.diagram.JCellcycletimeDiagram;
import de.hs.mannheim.modUro.diagram.JTimeSeriesDiagram;
import de.hs.mannheim.modUro.model.TimeSeries;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.diagram.SimulationDiagram;
import de.hs.mannheim.modUro.reader.*;
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
 * SimulationDiagramController controls SimulationDiagramView.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationDiagramController {

    private SimulationDiagram simulationDiagram;

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

    private Simulation simulation;

    public void init(Simulation simulation) {
        this.simulation = simulation;
        this.simulationDiagram = new SimulationDiagram(simulation);

        if (leftLastSelectedIndex == null || rightLastSelectedIndex == null) {
            initializeChoiceboxContent();
        } else {
            if (simulationContainsMetricType()) {
                setChoiceBoxContent();
                setLeftChartContent(leftLastSelectedIndex);
                setRightChartContent(rightLastSelectedIndex);
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
                setLeftChartContent(newValue.intValue());
                leftLastSelectedIndex = newValue.intValue();
                leftLastSelectedMetrictypename = choiceBoxMetrictypeNames().get(leftLastSelectedIndex);
            }
        });

        rightMetricType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setRightChartContent(newValue.intValue());
                rightLastSelectedIndex = newValue.intValue();
                rightLastSelectedMetrictypename = choiceBoxMetrictypeNames().get(rightLastSelectedIndex);
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
        List<String> name = choiceBoxMetrictypeNames();

        if (name.contains(leftLastSelectedMetrictypename) && name.contains(rightLastSelectedMetrictypename)) {
            containsMetricType = true;
        }

        return containsMetricType;
    }

    /**
     * Initializes Choicebox Content.
     */
    private void initializeChoiceboxContent() {
        List<String> name = choiceBoxMetrictypeNames();

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

        setLeftChartContent(left);
        setRightChartContent(right);
    }

    private List<String> choiceBoxMetrictypeNames() {
        List<String> name = new ArrayList<>();
        for (TimeSeries metricTypeItem : simulationDiagram.getTimeSeries()) {
            name.add(metricTypeItem.getName());
        }
        // Here we add - for now - the two generic diagram types:
        name.add("Cell count");
        name.add("Cell cycle times");

        return name;
    }

    /**
     * Sets Content of Choicebox.
     */
    private void setChoiceBoxContent() {
        List<String> name = choiceBoxMetrictypeNames();

        leftMetricType.setItems(FXCollections.observableArrayList(name));
        rightMetricType.setItems(FXCollections.observableArrayList(name));

        leftMetricType.getSelectionModel().select(leftLastSelectedIndex.intValue());
        rightMetricType.getSelectionModel().select(rightLastSelectedIndex.intValue());

    }

    /**
     * Sets left Chartcontent.
     *
     * @param selectedItemIndex
     */
    private void setLeftChartContent(int selectedItemIndex) {
        setChartContent(selectedItemIndex, leftPane);
    }

    /**
     * Sets right Chartcontent.
     *
     * @param selectedItemIndex
     */
    private void setRightChartContent(int selectedItemIndex) {
        setChartContent(selectedItemIndex, rightPane);
    }

    private void setChartContent(int selectedItemIndex, BorderPane pane) {
        // Very quick and dirty: TODO
        // New diagrams obtain getTimePointsSize and getTimePointsSize+1.
        if (selectedItemIndex == simulationDiagram.getTimeSeries().size()) {
            // This means cell count.
            CelltimesReader ctr = simulation.getCellTimesReader();
            if (ctr != null) {
                JCellCountDiagram ccd =
                        new JCellCountDiagram(ctr.getCellTypes(), ctr.getNumberOfCells());
                ChartViewer viewer = new ChartViewer(ccd);
                pane.setCenter(viewer);
            } else {
                // Cell count not available.
            }
        } else if (selectedItemIndex == simulationDiagram.getTimeSeries().size() + 1) {
            // And this means cell cycle times.
            CelltimesReader ctr = simulation.getCellTimesReader();
            if (ctr != null) {
                // TODO Q&D:
                CellCycleStat stat =
                        new CellCycleStat(ctr.getCellTypes(), ctr.getCycletimes());
                JCellcycletimeDiagram ctd =
                        new JCellcycletimeDiagram(ctr.getCellTypes(), ctr.getCycletimes());
                ChartViewer viewer = new ChartViewer(ctd);
                pane.setCenter(viewer);
            } else {
                // Cell count not available.
            }
        } else {
            TimeSeries timeSeries = simulationDiagram.getTimeSeries().get(selectedItemIndex);
            Diagram diagram = new JTimeSeriesDiagram(timeSeries);
            ChartViewer viewer = new ChartViewer(diagram);
            pane.setCenter(viewer);
        }
        pane.layout();
    }
}
