package de.hs.mannheim.modUro.controller.diagram;

import de.hs.mannheim.modUro.controller.diagram.fx.ChartViewer;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.diagram.BoxAndWhiskerPlotModel;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.awt.*;
import java.util.*;

/**
 * BoxAndWhiskerPlotController controls BoxAndWhiskerView.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class BoxAndWhiskerPlotController {

    //Reference to BoxAndWhiskerPlotModel
    private BoxAndWhiskerPlotModel boxAndWhiskerPlotModel;

    @FXML
    private TitledPane boxWhiskerPane;

    private Set<String> models;
    private Map<String, StatisticValues> stats;


    public void init(Project project) {
        this.boxAndWhiskerPlotModel = new BoxAndWhiskerPlotModel(project);
        models = new HashSet<>(boxAndWhiskerPlotModel.getModelTypeName());
        stats = new HashMap<>();
        stats = boxAndWhiskerPlotModel.getStatisticValues();

        boxWhiskerPlot();
    }

    private void boxWhiskerPlot(){
        BoxAndWhiskerCategoryDataset dataset = createDataset();
        CategoryAxis xAxis = new CategoryAxis("Model");
        NumberAxis yAxis = new NumberAxis("Fitness");

        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
                "Model comparison",
                new Font("Palatino", Font.BOLD, 14),
                plot,
                true
        );

        ChartViewer viewer = new ChartViewer(chart);
        boxWhiskerPane.setContent(viewer);

    }

    /**
     * Creates dataset for BoxWhiskerPlot.
     * @return
     */
    private BoxAndWhiskerCategoryDataset createDataset() {

        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        for (String model: models) {
            StatisticValues stat = stats.get(model);
            BoxAndWhiskerItem item = new BoxAndWhiskerItem(stat.getMean(), stat.getSecondPercentile(), stat.getFirstPercentile(), stat.getLastPercentile(), stat.getMin(), stat.getMax(), stat.getMin(), stat.getMax(), new ArrayList<>());
            dataset.add(item, model, model);
        }
        return dataset;
    }
    

    /**
     * Creates dataset for BoxWhiskerPlot.
     * @return
     */
    private BoxAndWhiskerCategoryDataset createDataset1() {

        final int seriesCount = 3;
        final int categoryCount = 4;
        final int entityCount = 22;

       // BoxAndWhiskerCategoryDataset

        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        for (int i = 0; i < seriesCount; i++) {
            for (int j = 0; j < categoryCount; j++) {
                final ArrayList list = new ArrayList();
                // add some values...
                for (int k = 0; k < entityCount; k++) {
                    final double value1 = 10.0 + Math.random() * 3;
                    list.add(new Double(value1));
                    final double value2 = 11.25 + Math.random(); // concentrate values in the middle
                    list.add(new Double(value2));
                }
                dataset.add(list, "Series " + i, " Type " + j);
            }
        }

        return dataset;
    }

}
