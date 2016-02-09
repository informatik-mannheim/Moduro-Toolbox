package de.hs.mannheim.modUro.controller.diagram;

import de.hs.mannheim.modUro.controller.diagram.fx.ChartViewer;
import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.diagram.ModeltypeDiagram;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TitledPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ModeltypeDiagramController controls ModelDiagramView.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeDiagramController {

    //Reference to ModelDiagram
    private ModeltypeDiagram modeltypeDiagram;

    @FXML
    private TitledPane leftPane;
    @FXML
    private TitledPane rightPane;
    @FXML
    private ChoiceBox leftMetricType;
    @FXML
    private ChoiceBox rightMetricType;

    private static Integer leftLastSelectedIndex;
    private static Integer rightLastSelectedIndex;

    private static String leftLastSelectedMetrictypename;
    private static String rightLastSelectedMetrictypename;


    public void init(ModelType modeltype){
        this.modeltypeDiagram = new ModeltypeDiagram(modeltype);

        if(leftLastSelectedIndex == null || rightLastSelectedIndex == null) {
            initializeChoiceboxContent();
        } else {
            if(simulationContainsMetricType()) {
                setChoiceBoxContent();
                setLeftChartContent(modeltypeDiagram.getMetricTypeName().get(leftLastSelectedIndex));
                setRightChartContent(modeltypeDiagram.getMetricTypeName().get(rightLastSelectedIndex));
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
                setLeftChartContent(modeltypeDiagram.getMetricTypeName().get(newValue.intValue()));
                leftLastSelectedIndex = newValue.intValue();
                leftLastSelectedMetrictypename = modeltypeDiagram.getMetricTypeName().get(leftLastSelectedIndex);

            }
        });

        rightMetricType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setRightChartContent(modeltypeDiagram.getMetricTypeName().get(newValue.intValue()));
                rightLastSelectedIndex = newValue.intValue();
                rightLastSelectedMetrictypename = modeltypeDiagram.getMetricTypeName().get(rightLastSelectedIndex);
            }
        });
    }

    /**
     * Checks if simultion has the last selected Metrictype from another simultion.
     * @return
     */
    private boolean simulationContainsMetricType() {
        boolean containsMetricType = false;
        List<String> name = modeltypeDiagram.getMetricTypeName();

        if(name.contains(leftLastSelectedMetrictypename) && name.contains(rightLastSelectedMetrictypename)) {
            containsMetricType = true;
        }

        return containsMetricType;
    }

    /**
     * Initializes Choicebox Content.
     */
    private void initializeChoiceboxContent() {
        List<String> name = modeltypeDiagram.getMetricTypeName();

        int left = 0;
        int right = 0;

        for (String val: name) {
            if(val.equals("FitnessArrangement")) {
                left = name.indexOf("FitnessArrangement");
            }

            if(val.equals("FitnessVolume")) {
                right = name.indexOf("FitnessVolume");
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

        setLeftChartContent(name.get(left));
        setRightChartContent(name.get(right));
    }


    /**
     * Sets Content of Choicebox.
     */
    private void setChoiceBoxContent(){
        List<String> name = modeltypeDiagram.getMetricTypeName();

        leftMetricType.setItems(FXCollections.observableArrayList(name));
        rightMetricType.setItems(FXCollections.observableArrayList(name));

        leftMetricType.getSelectionModel().select(leftLastSelectedIndex.intValue());
        rightMetricType.getSelectionModel().select(rightLastSelectedIndex.intValue());
    }


    /**
     * Sets left Chartcontent.
     * @param selectedItem
     */
    private void setLeftChartContent(String selectedItem){
        leftPane.setCollapsible(false);

        XYDataset dataset = createDataset(modeltypeDiagram.getSimulationList(), selectedItem);
        JFreeChart chart = createChart(dataset, selectedItem);
        chart.removeLegend();

        ChartViewer viewer = new ChartViewer(chart);
        leftPane.setContent(viewer);
    }

    /**
     * Sets right Chartcontent.
     * @param selectedItem
     */
    private void setRightChartContent(String selectedItem){
        rightPane.setCollapsible(false);

        XYDataset dataset = createDataset(modeltypeDiagram.getSimulationList(), selectedItem);
        JFreeChart chart = createChart(dataset, selectedItem);
        chart.removeLegend();

        ChartViewer viewer = new ChartViewer(chart);
        rightPane.setContent(viewer);
    }

    /**
     * Creates JFreeChart. XYLineDiagram.
     * @param dataset
     * @return
     */
    private static JFreeChart createChart(XYDataset dataset, String metricTypeName) {
        String title = metricTypeName;

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                title,    // title
                "t",      // x-axis label
                "f",      // y-axis label
                dataset);

        String fontName = "Palatino";
        xyLineChart.getTitle().setFont(new Font(fontName, Font.BOLD, 18));
        // TODO


        XYPlot plot = (XYPlot) xyLineChart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getDomainAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        plot.getRangeAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getRangeAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(false);
            renderer.setDrawSeriesLineAsPath(true);
            // set the default stroke for all series
            renderer.setAutoPopulateSeriesStroke(false);
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, new Color(24, 123, 58));
            renderer.setSeriesPaint(2, new Color(149, 201, 136));
            renderer.setSeriesPaint(3, new Color(1, 62, 29));
            renderer.setSeriesPaint(4, new Color(81, 176, 86));
            renderer.setSeriesPaint(5, new Color(0, 55, 122));
            renderer.setSeriesPaint(6, new Color(0, 92, 165));
        }

        return xyLineChart;

    }

    /**
     * Creates Dataset.
     * @return
     */
    private static XYDataset createDataset(List<Simulation> simulationList, String selectedItem) {

        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Simulation simualtionItem:simulationList) {
            XYSeries xySerie = new XYSeries(simualtionItem.getSimulationName());

            for (MetricType metricTypeItem: simualtionItem.getMetricType()) {
                if(metricTypeItem.getName().equals(selectedItem)) {
                    double x;
                    double y;
                    double[][] fitnessArray = metricTypeItem.getMetricData();

                    for(int i = 0; i<fitnessArray.length; i++) {
                        x =  fitnessArray[i][0];
                        y =  fitnessArray[i][1];
                        xySerie.add(x,y);
                    }
                }
            }
            dataset.addSeries(xySerie);
        }
        return dataset;
    }


}
