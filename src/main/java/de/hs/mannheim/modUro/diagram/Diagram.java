package de.hs.mannheim.modUro.diagram;

import org.jfree.chart.JFreeChart;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public abstract class Diagram {

    public abstract JFreeChart getJFreeChart();

    public String exportToTikz() {
        return "% Tikz-Export not implemented.";
    }

    public String exportToWSV() {
        return "# White-space separated values export not implemented.";
    }
}
