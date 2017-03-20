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
package de.hs.mannheim.modUro.diagram;

import de.hs.mannheim.modUro.model.TimeSeries;
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
import java.util.List;

/**
 * Creates a XY diagram where the x-axis represents the time.
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class JTimeSeriesDiagram extends Diagram {

    private JFreeChart chart;
    private List<TimeSeries> timeSeriesList;
    private TimeSeries timeSeries;
    private String name;

    /**
     * Creates an diagram that uses one time series, i.e. x-axis
     * is the time, and one to many series on the y-axis.
     * The name of the diagram is taken from the time series.
     *
     * @param timeSeries
     */
    public JTimeSeriesDiagram(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
        XYDataset dataset = createDataset(timeSeries);
        chart = createChart(dataset, timeSeries.getName());
    }

    /**
     * Creates an diagram that uses one or more time series, i.e. x-axis
     * is the time, and y-axis shows a union of all time series added.
     *
     * @param name           Name of the diagram.
     * @param timeSeriesList
     */
    public JTimeSeriesDiagram(String name, List<TimeSeries> timeSeriesList) {
        this.name = name;
        this.timeSeriesList = timeSeriesList;
        XYDataset dataset = createDatasetList(timeSeriesList);
        chart = createChart(dataset, name);
    }

    public JFreeChart getJFreeChart() {
        return chart;
    }

    public String exportToTikz() {
        return "% Tikz-Export not implemented.";
    }

    /**
     * Creates a string which lists all the values of the diagram
     * separated by white spaces.
     *
     * @return
     */
    public String exportToWSV() {
        StringBuffer sb = new StringBuffer();
        if (timeSeries != null) { // Just one time series.
            sb.append("# Time series: " + timeSeries.getName());
            sb.append("\n# Fields:");
            sb.append("\n# time\t");
            for (String dataName : timeSeries.getDataSeriesNames()) {
                sb.append(dataName + "\t");
            }
            for (int i = 0; i < timeSeries.getTimePointsSize(); i++) {
                sb.append("\n" + timeSeries.getTimeSeries()[i] + "\t");
                for (String dataName : timeSeries.getDataSeriesNames()) {
                    sb.append(timeSeries.getData(dataName)[i] + "\t");
                }
            }
        } else {
            sb.append("# time\t" + name);
        }
        return sb.toString();
    }

    private JFreeChart createChart(XYDataset dataset, String name) {
        String title = name;

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                title,    // title
                "t",      // x-axis label
                "f",      // y-axis label
                dataset);

        String fontName = "Palatino";
        xyLineChart.getTitle().setFont(new Font(fontName, Font.BOLD, 18));

        XYPlot plot = (XYPlot) xyLineChart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getDomainAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        plot.getRangeAxis().setLowerMargin(0.0);
        plot.getRangeAxis().setRange(0.0, 1.01);
        plot.getRangeAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getRangeAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        xyLineChart.getLegend().setItemFont(new Font(fontName, Font.PLAIN, 14));
        xyLineChart.getLegend().setFrame(BlockBorder.NONE);
        xyLineChart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
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

    private XYDataset createDataset(TimeSeries timeSeries) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (String dataName : timeSeries.getDataSeriesNames()) {
            XYSeries xySerie = new XYSeries(timeSeries.getName());
            for (int i = 0; i < timeSeries.getTimePointsSize(); i++) {
                double x = timeSeries.getTimeSeries()[i];
                double y = timeSeries.getData(dataName)[i];
                xySerie.add(x, y);
            }
            dataset.addSeries(xySerie);
        }
        return dataset;
    }

    private XYDataset createDatasetList(List<TimeSeries> timeSeriesList) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int n = 1;
        for (TimeSeries timeSeries : timeSeriesList) {
            XYSeries xySerie = new XYSeries(n++ + "");
            for (int i = 0; i < timeSeries.getTimePointsSize(); i++) {
                double x = timeSeries.getTimeSeries()[i];
                double y = timeSeries.getData()[i];
                xySerie.add(x, y);
            }
            dataset.addSeries(xySerie);
        }
        return dataset;
    }
}