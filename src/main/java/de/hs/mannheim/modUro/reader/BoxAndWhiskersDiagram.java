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
package de.hs.mannheim.modUro.reader;

import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.diagram.bawpModle;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class BoxAndWhiskersDiagram extends Diagram {

    public JFreeChart chart;
    private bawpModle bwpModel;

    public BoxAndWhiskersDiagram(bawpModle bwpModel) {
        this.bwpModel = bwpModel;
        boxWhiskerPlot();
    }

    public JFreeChart getJFreeChart() {
        return chart;
    }

    public String exportToTikz() {
        return "% Tikz-Export not implemented.";
    }

    public String exportToWSV() {
        return "# White-space separated values export not implemented.";
    }

    /**
     * Plots Data for Chart
     */
    private void boxWhiskerPlot() {
        BoxAndWhiskerCategoryDataset dataset = createDataset();
        CategoryAxis xAxis = new CategoryAxis("Model");
        NumberAxis yAxis = new NumberAxis("Fitness");
        yAxis.setRange(0.0, 1.0);

        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        renderer.setMaximumBarWidth(0.2);
        renderer.setItemMargin(0.5);
        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);

        chart = new JFreeChart(
                "Model comparison",
                new Font("Palatino", Font.BOLD, 14),
                plot,
                true
        );
        chart.removeLegend();
        // boxWhiskerPane.setCenter(viewer);
    }

    /**
     * Creates dataset for BoxWhiskerPlot.
     *
     * @return
     */
    private BoxAndWhiskerCategoryDataset createDataset() {

        DefaultBoxAndWhiskerCategoryDataset dataset =
                new DefaultBoxAndWhiskerCategoryDataset();

        List<String> sortedModels = bwpModel.getModelTypeName();
        sortedModels.sort(String::compareTo);
        for (String model : sortedModels) {
            StatisticValues stat = bwpModel.getStatisticValues().get(model);
            BoxAndWhiskerItem item = new BoxAndWhiskerItem(stat.getMean(), stat.getSecondPercentile(), stat.getFirstPercentile(), stat.getLastPercentile(), stat.getMin(), stat.getMax(), stat.getMin(), stat.getMax(), new ArrayList<>());
            // Second parameter is the row key (?), using always the same works:
            int n = stat.size();
            dataset.add(item, "", model + " (n=" + n + ")");
        }
        return dataset;
    }
}