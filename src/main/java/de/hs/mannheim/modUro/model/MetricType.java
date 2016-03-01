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

import java.util.Formatter;
import java.util.Locale;

/**
 * Model class for a MetricType.
 *
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

    public double getMean() {
        return mean;
    }

    public double getDeviation() {
        return deviation;
    }

    public String getMeanAsString() {
        return String.format("%1$.2f", mean).toString();
    }

    public String getDeviationAsString() {
        return String.format("%1$.2f", deviation).toString();
    }
}