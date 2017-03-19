package de.hs.mannheim.modUro.reader;

import java.util.Map;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class CellCycletimeEntry {
    public double time;
    public Map<String, Double> meanValues;

    public CellCycletimeEntry(double time, Map<String, Double> meanValues) {
        this.time = time;
        this.meanValues = meanValues;
    }

    public String toString() {
        return time + ": " + meanValues;
    }
}
