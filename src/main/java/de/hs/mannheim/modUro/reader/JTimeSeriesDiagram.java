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
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class JTimeSeriesDiagram extends Diagram {

    public JFreeChart chart;

    public JTimeSeriesDiagram(TimeSeries timeSeries) {
        XYDataset dataset = createDataset(timeSeries);
        chart = createChart(dataset, timeSeries.getName());
    }

    public JTimeSeriesDiagram(String name, List<TimeSeries> timeSeriesList) {
        XYDataset dataset = createDatasetList(timeSeriesList);
        chart = createChart(dataset, name);
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
        for (int s = 0; s < timeSeries.getNumberOfDataSeries(); s++) {
            XYSeries xySerie = new XYSeries(timeSeries.getName());
            for (int i = 0; i < timeSeries.getTimeSeriesSize(); i++) {
                double x = timeSeries.getTimeSeries()[i];
                double y = timeSeries.getData(s)[i];
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
            for (int i = 0; i < timeSeries.getTimeSeriesSize(); i++) {
                double x = timeSeries.getTimeSeries()[i];
                double y = timeSeries.getData()[i];
                xySerie.add(x, y);
            }
            dataset.addSeries(xySerie);
        }
        return dataset;
    }
}