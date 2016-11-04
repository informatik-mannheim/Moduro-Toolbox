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

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Class for StaticticValues used in Box-Whisker-Plot.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class StatisticValues {

    private String metricTypeName;
    private double mean;
    private double variance;
    private double stdDev;
    private double firstPercentile;     //25%
    private double secondPercentile;    //50%, also median
    private double lastPercentile;      //75%
    private double min;
    private double max;

    DescriptiveStatistics stats = new DescriptiveStatistics();

    public StatisticValues(String metricTypeName, double[] array) {
        this.metricTypeName = metricTypeName;

        for (int i = 0; i < array.length; i++) {
            stats.addValue(array[i]);
        }

        this.mean = stats.getMean();
        this.variance = stats.getVariance();
        this.stdDev = stats.getStandardDeviation();
        this.firstPercentile = stats.getPercentile(25);
        this.secondPercentile = stats.getPercentile(50);
        this.lastPercentile = stats.getPercentile(75);
        this.min = stats.getMin();
        this.max = stats.getMax();
    }

    public String getMetricTypeName() {
        return metricTypeName;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public double getFirstPercentile() {
        return firstPercentile;
    }

    public double getSecondPercentile() {
        return secondPercentile;
    }

    public double getLastPercentile() {
        return lastPercentile;
    }

    public double getMin() {
        return min;
    }

    public double getStdDev() {
        return stdDev;
    }

    public double getMax() {
        return max;
    }

    public String getMeanAsString() {
        return String.format("%1$.2f", mean).toString();
    }

    public String getStdDevAsString() {
        return String.format("%1$.2f", stdDev).toString();
    }
}
