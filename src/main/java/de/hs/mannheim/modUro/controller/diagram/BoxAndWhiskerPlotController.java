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

import de.hs.mannheim.modUro.controller.diagram.fx.ChartViewer;
import de.hs.mannheim.modUro.model.Project;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.diagram.BoxAndWhiskerPlotModel;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
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
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class BoxAndWhiskerPlotController {

    //Reference to BoxAndWhiskerPlotModel
    private BoxAndWhiskerPlotModel boxAndWhiskerPlotModel;

    @FXML
    private BorderPane boxWhiskerPane;

    private Set<String> models;
    private Map<String, StatisticValues> stats;

    public void init(Project project) {
        this.boxAndWhiskerPlotModel = new BoxAndWhiskerPlotModel(project);
        models = new HashSet<>(boxAndWhiskerPlotModel.getModelTypeName());
        stats = new HashMap<>();
        stats = boxAndWhiskerPlotModel.getStatisticValues();

        boxWhiskerPlot();
    }

    /**
     * Plots Data for Chart
     */
    private void boxWhiskerPlot() {
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
        boxWhiskerPane.setCenter(viewer);
    }

    /**
     * Creates dataset for BoxWhiskerPlot.
     *
     * @return
     */
    private BoxAndWhiskerCategoryDataset createDataset() {

        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        for (String model : models) {
            StatisticValues stat = stats.get(model);
            BoxAndWhiskerItem item = new BoxAndWhiskerItem(stat.getMean(), stat.getSecondPercentile(), stat.getFirstPercentile(), stat.getLastPercentile(), stat.getMin(), stat.getMax(), stat.getMin(), stat.getMax(), new ArrayList<>());
            dataset.add(item, model, model);
        }
        return dataset;
    }
}
