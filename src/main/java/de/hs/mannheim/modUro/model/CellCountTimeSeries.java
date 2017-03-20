package de.hs.mannheim.modUro.model;

import de.hs.mannheim.modUro.reader.CellCountEntry;

import java.util.List;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class CellCountTimeSeries extends TimeSeries {

    public CellCountTimeSeries(List<String> cellTypes,
                               List<CellCountEntry> cellcountList) {
        super("Cell count");
        boolean timeSeriesSet = false;
        for (String cellType : cellTypes) {
            int n = cellcountList.size();
            double[] xData = new double[n];
            double[] yData = new double[n];
            int i = 0;
            for (CellCountEntry e : cellcountList) {
                double x = e.time;
                double y = 0;
                if (e.count.containsKey(cellType)) {
                    y = (double) e.count.get(cellType);
                }
                xData[i] = x;
                yData[i] = y;
                i++;
            }
            if (!timeSeriesSet) {
                setTimeSeries(xData);
                timeSeriesSet = true;
            }
            addDataSeries(cellType, yData);
        }
        double[] sumData = new double[getTimePointsSize()];
        int k = 0;
        for (CellCountEntry e : cellcountList) {
            sumData[k++] = e.count.values().stream().mapToDouble(i -> i.intValue()).sum();
        }
        addDataSeries("total", sumData);
    }
}
