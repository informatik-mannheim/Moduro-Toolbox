package de.hs.mannheim.modUro.model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Class for StaticticValues used in Box-Whisker-Plot.
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

    public StatisticValues(double[] array) {

        for( int i = 0; i < array.length; i++) {
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

    public StatisticValues(String metricTypeName, double mean, double stdDev) {

        this.metricTypeName = metricTypeName;
        this.mean = mean;
        this.stdDev = stdDev;
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
}
