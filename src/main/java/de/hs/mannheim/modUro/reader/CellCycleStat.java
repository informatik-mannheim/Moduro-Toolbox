package de.hs.mannheim.modUro.reader;

import de.hs.mannheim.modUro.model.StatisticValues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class CellCycleStat {

    private Map<String, StatisticValues> m = new HashMap<>();

    public CellCycleStat(List<String> cellTypes,
                         List<CellCycletimeEntry> cycletimesList) {
        for (String celltype : cellTypes) {
            double[] means = cycletimesList.stream().
                    filter(e -> e.meanValues.containsKey(celltype)).
                    map(e -> e.meanValues.get(celltype)).
                    filter(e -> !Double.isNaN(e)).
                    mapToDouble(Double::doubleValue).toArray();
            StatisticValues stat = new StatisticValues(celltype, means);
            m.put(celltype, stat);
        }
    }

    public StatisticValues getStatValues(String celltype) {
        return m.get(celltype);
    }

    public String toString() {
        String s = "";
        for (String name : m.keySet()) {
            s += m.get(name).toString() + "\n";
        }
        return s;
    }
}
