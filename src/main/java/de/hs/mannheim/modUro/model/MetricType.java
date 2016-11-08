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
public class MetricType extends StatisticValues {

    private double[][] metricData;

    public MetricType(String name, double[][] metricData) {
        super(name, extractColumn(metricData, 1));
        this.metricData = metricData;
    }

    public double[][] getMetricData() {
        return metricData;
    }

    private static double[] extractColumn(double[][] data, int idx) {
        double[] a = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            a[i] = data[i][idx];
        }
        return a;
    }
}