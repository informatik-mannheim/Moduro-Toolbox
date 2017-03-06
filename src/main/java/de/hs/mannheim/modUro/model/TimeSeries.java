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
public class TimeSeries extends StatisticValues {

    //Input file of a metric type txt file
    private File file;

    private double[][] metricData;

    public TimeSeries(File file) {
        super(getName(file));
        this.file = file;
        metricData = readMetricDataFromFile();
        init(extractColumn(metricData, 1));
    }

    public TimeSeries(String name, double[][] metricData) {
        super(name, extractColumn(metricData, 1));
        this.metricData = metricData;
    }

    public double[][] getData() {
        return metricData;
    }

    public double getMaxTime() {
        return metricData[metricData.length - 1][0];
    }

    public int size() {
        return metricData.length;
    }

    private static double[] extractColumn(double[][] data, int idx) {
        double[] a = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            a[i] = data[i][idx];
        }
        return a;
    }

    /**
     * Parses name of TimeSeries.
     *
     * @return
     */
    private static String getName(File file) {
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
    private double[][] readMetricDataFromFile() {
        //initialize matrix length with line length of file
        double[][] metricData = new double[1][2];
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
        return metricData;
    }

    public File getFile() {
        return file;
    }
}
