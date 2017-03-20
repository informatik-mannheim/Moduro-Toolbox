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

import de.hs.mannheim.modUro.config.CellTypeColor;
import de.hs.mannheim.modUro.model.TimeSeries;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;

import java.awt.*;
import java.util.Set;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class JCellCountDiagram extends JTimeSeriesDiagram {

    public JCellCountDiagram(TimeSeries timeSeries) {
        super(timeSeries, false);
    }

    protected JFreeChart createChart(XYDataset dataset, String name) {
        String title = name;

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                title,    // title
                "t",      // x-axis label
                "n",      // y-axis label
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
        // plot.getRangeAxis().setRange(0.0, 1.01);
        plot.getRangeAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getRangeAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.gray);
        xyLineChart.getLegend().setItemFont(new Font(fontName, Font.PLAIN, 14));
        xyLineChart.getLegend().setFrame(BlockBorder.NONE);
        xyLineChart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
        XYItemRenderer r = plot.getRenderer();
        // set the default stroke for all series
        int i = 0;
        for (String celltype : getTimeSeries().getDataSeriesNames()) {
            r.setSeriesPaint(i, CellTypeColor.getColor(celltype));
            i++;
        }
        r.setSeriesPaint(i, Color.BLACK);
        return xyLineChart;
    }
}
