package de.hs.mannheim.modUro.model;

import java.util.Formatter;
import java.util.Locale;

/**
 * Model class for a MetricType.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class MetricType {

    private String name;
    private double[][] metricData;
    private double mean;
    private double deviation;

    public MetricType(String name, double[][] metricData, double mean, double deviation) {
        this.name = name;
        this.metricData = metricData;
        this.mean = mean;
        this.deviation = deviation;
    }

    public String getName() {
        return name;
    }

    public double[][] getMetricData() {
        return metricData;
    }

    public double getMean() {return mean;}

    public double getDeviation() {return deviation;}

    public String getMeanAsString() { return String.format("%1$.2f", mean).toString(); }

    public String getDeviationAsString() { return String.format("%1$.2f", deviation).toString(); }
}