package de.hs.mannheim.modUro.reader;

import de.hs.mannheim.modUro.model.StatisticValues;
import de.hs.mannheim.modUro.model.TimeSeries;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class CellCycleStat {

    private Map<String, TimeSeries> m = new HashMap<>();
    private List<String> cellTypes;

    public CellCycleStat(List<String> cellTypes,
                         List<CellCycletimeEntry> cycletimesList) {
        this.cellTypes = cellTypes;

        for (String cellType : cellTypes) {
            double[] timeSeries = new double[cycletimesList.size()];
            double[] dataSeries = new double[cycletimesList.size()];
            int i = 0;
            for (CellCycletimeEntry e : cycletimesList) {
                double x = e.time;
                double y = Double.NaN;
                if (e.meanValues.containsKey(cellType)) {
                    y = e.meanValues.get(cellType);
                }
                timeSeries[i] = x;
                dataSeries[i] = y;
                i++;
            }
            TimeSeries ts = new TimeSeries(cellType, timeSeries,
                    cellType, dataSeries);
            m.put(cellType, ts);
        }
    }

    public TimeSeries getTimeSeries(String celltype) {
        return m.get(celltype);
    }

    public List<String> getCellTypes() {
        return cellTypes;
    }

    public String toString() {
        String s = "";
        for (String name : m.keySet()) {
            s += m.get(name).toString() + "\n";
        }
        return s;
    }
}
