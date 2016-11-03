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
package de.hs.mannheim.modUro.creator;

import de.hs.mannheim.modUro.model.MetricType;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MetricTypeCreator helps to create a MetricType by passing a file txt.
 *
 * @author Mathuraa Pathmanathan
 */
public class MetricTypeCreator {

    //Input file of a metric type txt file
    private File file;

    //MetricType Instance
    private MetricType metricType;

    // Get a DescriptiveStatistics instance (calculation of mean and stdDev)
    DescriptiveStatistics stats = new DescriptiveStatistics();

    /**
     * Constructor
     */
    public MetricTypeCreator() {
    }

    /**
     * Parses name of MetricType.
     *
     * @return
     */
    private String nameOfMetricType() {
        String name = null;
        int pos = file.getName().lastIndexOf(".");  //searches the pos of last index of "."
        if (pos != -1) {
            name = file.getName().substring(0, pos);   //substring the Filename at the last "."
        }
        return name;
    }

    private double[][] metricData;

    /**
     * Reads and parse MetricData from File.
     * TODO This is redundant with subroutine in SimulationCreator!
     */
    private void readMetricDataFromFile() {
        //initialize matrix length with line length of file
        metricData = new double[1][2];
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
        metricData = new double[row][2];
        for (int i = 0; i < row; i++) {
            metricData[i][0] = times.get(i);
            metricData[i][1] = fitness.get(i);
        }
    }

    /**
     * Calculates mean of metric data.
     *
     * @return
     */
    private double calculateMean() {
        // Add the data from the array
        for (int i = 0; i < metricData.length; i++) {
            stats.addValue(metricData[i][1]);
        }
        double mean = stats.getMean();
        stats.clear();
        return mean;
    }

    /**
     * Calculates deviation of metric data.
     *
     * @return
     */
    private double calculateDeviation() {
        // Add the data from the array
        for (int i = 0; i < metricData.length; i++) {
            stats.addValue(metricData[i][1]);
        }
        double stdDev = stats.getStandardDeviation();
        stats.clear();
        return stdDev;
    }

    /**
     * Creates a MetricType Instance.
     */
    public void createMetricType() {
        readMetricDataFromFile();
        this.metricType = new MetricType(nameOfMetricType(),
                metricData, calculateMean(), calculateDeviation());
    }

    public File getFile() {
        return file;
    }

    /**
     * Sets a txt file of a MetricType.
     *
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Returns created MetricType.
     *
     * @return
     */
    public MetricType getMetricType() {
        return metricType;
    }
}
