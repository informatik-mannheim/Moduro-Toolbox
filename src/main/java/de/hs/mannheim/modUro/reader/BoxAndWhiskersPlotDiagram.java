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

import de.hs.mannheim.modUro.diagram.Diagram;
import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.diagram.BoxAndWhiskerPlotModel;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A diagram that shows a Box and Whisker plot.
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class BoxAndWhiskersPlotDiagram extends Diagram {

    private JFreeChart chart;
    private BoxAndWhiskerPlotModel bwpModel;

    /**
     * Create the diagram.
     * @param bwpModel Related model.
     */
    public BoxAndWhiskersPlotDiagram(BoxAndWhiskerPlotModel bwpModel) {
        this.bwpModel = bwpModel;
        boxWhiskerPlot();
    }

    public JFreeChart getJFreeChart() {
        return chart;
    }

    /**
     * Create a Tikz snippet that will render a Box and Whisker diagram
     * in Tikz.
     * @return
     */
    public String exportToTikz() {
        Map<String, StatisticValues> stats = bwpModel.getStatisticValues();
        List<String> sortedModels = new ArrayList<>(stats.keySet());
        //sortedModels.sort(String::compareTo);
        sortedModels.sort((s1, s2) -> s2.compareTo(s1));
        String ytickslabels = "yticklabels={" +
                sortedModels.stream().map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                "}";
        List<String> yticksl = new ArrayList<>();
        for (int i = 2; i <= sortedModels.size() + 1; i++) {
            yticksl.add(i + "");
        }
        String ytick = "ytick={" +
                yticksl.stream().map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                "}";
        String enn = "enn"; // ok.map(m = > m._1.toString + " = " + m._2.toString).mkString(", ")
        String s = "% " + enn + "\n";
        s = s + "\\begin{tikzpicture}\n" +
                " \\begin{axis}[\n" +
                " " + ytick + ",\n" +
                " " + ytickslabels + ",\n " +
                " height=.5*\\textheight,\n" +
                " xmin=0, xmax=1.0, width=.9*\\textwidth,\n" +
                " xtick={0,.1,.2,.3,.4,.5,.6,.7,.8,.9,1},\n" +
                " xticklabels={0,.1,.2,.3,.4,.5,.6,.7,.8,.9,1}\n" +
                " ]\n";

        int pos = 2;
        for (String m : sortedModels) {
            s = s +
                    " \\addplot+[boxplot prepared={draw position=" + pos + ",\n" +
                    "  lower whisker=" + stats.get(m).getMin() +
                    ", lower quartile=" + stats.get(m).getFirstPercentile() + ",\n" +
                    "  median=" + stats.get(m).getSecondPercentile() + ", upper quartile=" +
                    stats.get(m).getLastPercentile() + ",\n" +
                    "  upper whisker=" + stats.get(m).getMax() + ",\n" +
                    "  every box/.style={draw=black},\n" +
                    "  every whisker/.style={black},\n" +
                    "  every median/.style={black}}]\n" +
                    " coordinates {};\n";
            pos++;
        }
        s = s + " \\end{axis}\n" +
                "\\end{tikzpicture}\n";
        return s;
    }

    @Override
    public String exportToWSV() {
        Map<String, StatisticValues> stats = bwpModel.getStatisticValues();
        List<String> sortedModels = new ArrayList<>(stats.keySet());
        sortedModels.sort((s1, s2) -> s2.compareTo(s1));
        String s = "\n# Model Data (Mean fitness) ...";
        for (String m : sortedModels) {
            double[] data = stats.get(m).getData();
            s += "\n" + m;
            for (double v : data) {
                s += " " + v;
            }
        }
        s += "\n";
        return s;
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