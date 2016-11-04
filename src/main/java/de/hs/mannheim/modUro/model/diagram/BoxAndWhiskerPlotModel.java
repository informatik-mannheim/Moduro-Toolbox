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
package de.hs.mannheim.modUro.model.diagram;


import de.hs.mannheim.modUro.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Class for BoxAndWhiskerPlotModel.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class BoxAndWhiskerPlotModel {

    private Project project;

    private List<String> modelTypeName;
    private Map<String, StatisticValues> statisticValues;

    public BoxAndWhiskerPlotModel(Project project) {
        this.project = project;
        modelTypeName = new ArrayList<>(); // TODO ???
        modelTypeName = listModelTypeName();
        statisticValues = new HashMap<>();
        calculateStatValues();
    }

    private void calculateStatValues() {

        for (ModelType modelTypeItem : project.getModelTypeList()) {
            List<Double> meanOfSimulations = new ArrayList<>();
            for (Simulation simulationItem : modelTypeItem.getSimulations()) {
                List<MetricType> metricType = simulationItem.getMetricTypes();
                double mean = 0.0;
                for (MetricType metricTypeItem : metricType) {
                    if (metricTypeItem.getName().contains("FitnessVolume")) {
                        mean = metricTypeItem.getMean();
                    }
                }
                meanOfSimulations.add(mean);
            }
            Double[] a = meanOfSimulations.toArray(new Double[0]);
            double[] meanArrayOfMeans = Stream.of(a).mapToDouble(Double::doubleValue).toArray();

            StatisticValues stat =
                    new StatisticValues(modelTypeItem.getName(), meanArrayOfMeans);
            statisticValues.put(modelTypeItem.getName(), stat);
        }
    }

    /**
     * Lists distinct names of model type.
     *
     * @return
     */
    private List<String> listModelTypeName() {
        List<String> modeltypeName = new ArrayList<>();
        for (ModelType modelTypeItem : project.getModelTypeList()) {
            if (!modeltypeName.contains(modelTypeItem.getName())) {
                modeltypeName.add(modelTypeItem.getName());
            }
        }
        return modeltypeName;
    }

    public List<String> getModelTypeName() {
        return modelTypeName;
    }

    public Map<String, StatisticValues> getStatisticValues() {
        return statisticValues;
    }
}
