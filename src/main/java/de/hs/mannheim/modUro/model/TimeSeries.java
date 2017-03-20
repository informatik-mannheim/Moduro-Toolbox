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
package de.hs.mannheim.modUro.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * A time series is a data where one to many series of data
 * can be stored for a time value. It is similar to a XY plot.
 * Each data series has associated descriptive statistics (mean etc.).
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class TimeSeries {

    private File file; // Input file of a metric type txt file.
    private String name;
    private double[] timeSeries;
    private Map<String, double[]> dataSeries = new HashMap<>();
    private Map<String, StatisticValues> stats = new HashMap<>();
    private String defaultDataSeries;

    /**
     * Creates a single time series from a file. A file's entry
     * is x y (separated by a white space).
     *
     * @param file
     */
    public TimeSeries(File file) {
        this.name = getFileName(file);
        this.file = file;
        readDataFromFile();
    }

    /**
     * Creates a time series with exactly one data series.
     *
     * @param name       Name of the time series.
     * @param timeSeries Time points.
     * @param dataSeries Data points (one for each time points).
     */
    public TimeSeries(String name, double[] timeSeries,
                      String dataName, double[] dataSeries) {
        this.name = name;
        this.timeSeries = timeSeries;
        addDataSeries(dataName, dataSeries);
    }

    /**
     * Add a new data series.
     *
     * @param dataName Name of the data series.
     * @param data     Data itself.
     */
    public void addDataSeries(String dataName, double[] data) {
        if (dataSeries.containsKey(dataName)) {
            throw new RuntimeException("Data series name " + dataName +
                    " already exists. Data series cannot be added.");
        }
        if (dataSeries.isEmpty()) {
            defaultDataSeries = dataName;
        } else {
            if (data.length != getTimePointsSize()) {
                throw new RuntimeException("New data series has different size" +
                        " and cannot be added. Is: " + data.length +
                        ", should " + getTimePointsSize());
            }
        }
        dataSeries.put(dataName, data);
        StatisticValues sv = new StatisticValues(name, data);
        stats.put(dataName, sv);
    }

    public String getName() {
        return name;
    }

    public Set<String> getDataSeriesNames() {
        return dataSeries.keySet();
    }

    /**
     * @return Time points.
     */
    public double[] getTimeSeries() {
        return timeSeries;
    }

    /**
     * @return The last time point.
     */
    public double getMaxTime() {
        return timeSeries[timeSeries.length - 1];
    }

    /**
     * @return The first data series.
     */
    public double[] getData() {
        return getData(defaultDataSeries);
    }

    public double[] getData(String dataName) {
        return dataSeries.get(dataName);
    }

    public StatisticValues getStats() {
        return getStats(defaultDataSeries);
    }

    public StatisticValues getStats(String dataName) {
        return stats.get(dataName);
    }

    /**
     * @return Number of time points.
     */
    public int getTimePointsSize() {
        return timeSeries.length;
    }

    /**
     * @return Number of data series (1 to n).
     */
    public int getNumberOfDataSeries() {
        return dataSeries.size();
    }

    public File getFile() {
        return file;
    }

    // Required for TableColumn in SimulationOverviewController.
    public String getMeanAsString() {
        return getStats().getMeanAsString();
    }

    public String getStdDevAsString() {
        return getStats().getStdDevAsString();
    }

    /**
     * Parses name of TimeSeries.
     *
     * @return
     */
    private String getFileName(File file) {
        String name = null;
        int pos = file.getName().lastIndexOf(".");  //searches the pos of last index of "."
        if (pos != -1) {
            name = file.getName().substring(0, pos);   //substring the Filename at the last "."
        }
        return name;
    }

    /**
     * Reads and parse TimeSeries data from file.
     */
    private void readDataFromFile() {
        String line;
        int row = 0;

        List<Double> times = new ArrayList<>();
        List<Double> fitness = new ArrayList<>();
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
            while ((line = buffer.readLine()) != null) {
                String[] vals = line.trim().split(" ");
                times.add(Double.parseDouble(vals[0]));
                fitness.add(Double.parseDouble(vals[1]));
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        timeSeries = new double[row];
        double[] data = new double[row];
        for (int i = 0; i < row; i++) {
            timeSeries[i] = times.get(i);
            data[i] = fitness.get(i);
        }
        addDataSeries(getName(), data);
    }
}
