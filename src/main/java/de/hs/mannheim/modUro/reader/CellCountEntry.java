package de.hs.mannheim.modUro.reader;

import java.util.Map;

/**
 * A cell count entry. It contains a time point (time) and a map that
 * contains the number of cells for this point in time. The key is the cell
 * type and the value the number of cells.
 */
public class CellCountEntry {
    public double time;
    public Map<String, Long> count;

    public CellCountEntry(double time, Map<String, Long> count) {
        this.time = time;
        this.count = count;
    }

    public String toString() {
        return time + ": " + count;
    }
}