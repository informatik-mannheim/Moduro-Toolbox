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
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for a TimeSeries.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class TimeSeries {

    private File file; // Input file of a metric type txt file.
    private String name;
    private double[] timeSeries;
    private List<double[]> dataSeries = new ArrayList<>();
    private List<StatisticValues> stats = new ArrayList<>();

    public TimeSeries(String name) {
        this.name = name;
    }

    /**
     * Reads a single times series from a file.
     *
     * @param file
     */
    public TimeSeries(File file) {
        this.name = getFileName(file);
        this.file = file;
        readDataFromFile();
    }

    public TimeSeries(String name, double[] timeSeries, double[] dataSeries) {
        this.name = name;
        this.timeSeries = timeSeries;
        addDataSeries(name, dataSeries);
    }

    public String getName() {
        return name;
    }

    public double[] getTimeSeries() {
        return timeSeries;
    }

    public double getMaxTime() {
        return timeSeries[timeSeries.length - 1];
    }

    public double[] getData() {
        return getData(0);
    }

    public double[] getData(int idx) {
        return dataSeries.get(idx);
    }

    public StatisticValues getStats() {
        return stats.get(0);
    }

    public StatisticValues getStats(int idx) {
        return stats.get(idx);
    }

    public int getTimeSeriesSize() {
        return timeSeries.length;
    }

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

    private void addDataSeries(String name, double[] data) {
        dataSeries.add(data);
        StatisticValues sv = new StatisticValues(name, data);
        stats.add(sv);
    }
}
