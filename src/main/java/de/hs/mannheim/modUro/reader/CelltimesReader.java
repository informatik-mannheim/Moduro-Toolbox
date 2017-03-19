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

import de.hs.mannheim.modUro.config.ToolboxLogger;
import de.hs.mannheim.modUro.model.TimeSeries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class reads the file Celltimes.daz and calculates 1) the
 * number of cells (per cell type) over time and 2) the average cell cycle
 * time over time per cell type.
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class CelltimesReader {

    private double deltaTCount; // Increment for cell count time steps in days.

    public CelltimesReader(String filename) throws IOException {
        this(filename, 0.5, 2.0);
    }

    /**
     * @param filename The file name (e.g. "Celltimes.daz").
     * @param deltaTCount   Increment for cell count time steps in days.
     * @throws IOException
     */
    public CelltimesReader(String filename, double deltaTCount,
                           double deltaTCycle) throws IOException {
        this.deltaTCount = deltaTCount;
        this.deltaTCycle = deltaTCycle;
        readFile(filename);
    }

    /**
     * @return A list of all cell types considered in this file.
     */
    public List<String> getCellTypes() {
        List<String> l = new ArrayList<String>(cellTypes);
        Collections.sort(l);
        return l;
    }

    /**
     * @return The number of cells in ascending time order.
     */
    public List<CellCountEntry> getNumberOfCells() {
        return cellcountList;
    }

    public List<CellCycletimeEntry> getCycletimes() {
        return cellcycletimesList;
    }

    /**
     * Parse the input file and creates the cell count and average cycle times.
     *
     * @param filename
     * @throws IOException
     */
    private void readFile(String filename) throws IOException {
        String line;
        int row = 0;
        BufferedReader buffer = new BufferedReader(new FileReader(filename));
        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split(" ");
            try {
                double time = Double.parseDouble(vals[0]);
                String op = vals[1];
                int cellId = Integer.parseInt(vals[2]);
                String cellType = toName(vals[3]);
                double lifetime = 0;
                if (op.equals("-")) {
                    lifetime = Double.parseDouble(vals[4]);
                }
                addCountData(time, op, cellId, cellType, lifetime);
                addCycleData(time, op, cellId, cellType, lifetime);
            } catch (NumberFormatException e) {
                ToolboxLogger.log.severe("Error when reading cell times at line " + row);
                ToolboxLogger.log.severe("Record is ignored.");
            }
            row++;
        }
        // Finally, close all open records:
        CellCountEntry ce = new CellCountEntry(endTimeIntervalNumber, new HashMap<String, Long>(cellcount));
        cellcountList.add(ce);
        calcAvgCycleTimes(endTimeIntervalCycle + deltaTCycle);
    }

    private List<CellCountEntry> cellcountList = new ArrayList<>();
    private Map<String, Long> cellcount = new HashMap<>();
    private Set<String> cellTypes = new HashSet();

    private double endTimeIntervalNumber = -deltaTCount;

    /**
     * Add a cell event for a time point.
     *
     * @param time
     * @param op
     * @param cellId
     * @param cellType
     * @param lifetime
     */
    private void addCountData(double time, String op, int cellId, String cellType,
                              double lifetime) {
        // Events and their time points can have gaps. The gaps
        // are filled with previous values:
        if (time - endTimeIntervalNumber >= deltaTCount) {
            int skips = (int) ((time - endTimeIntervalNumber) / deltaTCount);
            for (int i = 0; i < skips; i++) {
                endTimeIntervalNumber += deltaTCount;
                // Add entry:
                CellCountEntry ce = new CellCountEntry(endTimeIntervalNumber, new HashMap<String, Long>(cellcount));
                cellcountList.add(ce);
            }
        }
        switch (op) {
            case "+": {
                cellTypes.add(cellType); // Remember this cell type.
                long n = 0;
                if (cellcount.containsKey(cellType)) {
                    n = cellcount.get(cellType);
                }
                cellcount.put(cellType, n + 1);
                break;
            }
            case "-": {
                long n = cellcount.get(cellType);
                cellcount.put(cellType, n - 1);
                break;
            }
            default:
                throw new RuntimeException("Illegal operation: " + op);
        }
    }

    private double deltaTCycle = 2;
    private double endTimeIntervalCycle = 0;
    private List<CellCycleTimesPerInterval> cellTimes = new ArrayList();
    private List<CellCycletimeEntry> cellcycletimesList = new ArrayList<>();

    private void addCycleData(double time, String op, int cellId, String cellType,
                              double lifetime) {
        if ("-".equals(op)) {
            if (time - endTimeIntervalCycle >= deltaTCycle) {
                calcAvgCycleTimes(endTimeIntervalCycle + deltaTCycle);
                cellTimes = new ArrayList();
                int skips = (int) ((time - endTimeIntervalCycle) / deltaTCycle);
                endTimeIntervalCycle += skips * deltaTCycle;
            }
            cellTimes.add(new CellCycleTimesPerInterval(cellType, lifetime));
        }
    }

    /**
     * The list cellTimes needs now to be converted to average values.
     *
     * @param time Begin of interval.
     */
    private void calcAvgCycleTimes(double time) {
        Map<String, Double> meanValues = new HashMap<>();
        for (String celltype : getCellTypes()) {
            List<CellCycleTimesPerInterval> l =
                    cellTimes.stream().filter(e -> e.cellType.equals(celltype)).
                    collect(Collectors.toList());
            int n = l.size();
            double sum = l.stream().mapToDouble(e -> e.cycletime).sum();
            double avg = sum / n;
            meanValues.put(celltype, avg);
        }
        cellcycletimesList.add(new CellCycletimeEntry(time, meanValues));
    }

    private String toName(String celltypeId) {
        switch (celltypeId) {
            case "0":
                return "0: M";
            case "1":
                return "1: BM";
            case "2":
                return "2: S";
            case "3":
                return "3: B";
            case "4":
                return "4: I";
            case "5":
                return "5: U";
            default:
                return celltypeId;
        }
    }
}

/**
 * A cell count entry. It contains a time point (time) and a map that
 * contains the number of cells for this point in time. The key is the cell
 * type and the value the number of cells.
 */
class CellCountEntry {
    double time;
    Map<String, Long> count;

    public CellCountEntry(double time, Map<String, Long> count) {
        this.time = time;
        this.count = count;
    }

    public String toString() {
        return time + ": " + count;
    }
}

class CellCycletimeEntry {
    double time;
    Map<String, Double> meanValues;

    public CellCycletimeEntry(double time, Map<String, Double> meanValues) {
        this.time = time;
        this.meanValues = meanValues;
    }

    public String toString() {
        return time + ": " + meanValues;
    }
}

class CellCycleTimesPerInterval {

    String cellType;
    double cycletime;

    public CellCycleTimesPerInterval(String cellType, double cycletime) {
        this.cellType = cellType;
        this.cycletime = cycletime;
    }
}