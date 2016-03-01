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

/**
 * Class for BoxAndWhiskerPlotModel.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class BoxAndWhiskerPlotModel {

    Project project;

    List<String> modelTypeName;
    Map<String, StatisticValues> statisticValues;

    public BoxAndWhiskerPlotModel(Project project) {
        this.project = project;
        modelTypeName = new ArrayList<>();
        modelTypeName = listModelTypeName();
        statisticValues = new HashMap<>();

        calculateStatValues();
    }

    private void calculateStatValues() {
        List<Double> meanOfSimulations = new ArrayList<>();

        for (String value : modelTypeName) {
            for (ModelType modelTypeItem : project.getModelTypeList()) {
                if (modelTypeItem.getName().equals(value)) {
                    for (Simulation simultionItem : modelTypeItem.getSimulations()) {
                        List<MetricType> metricType = simultionItem.getMetricType();
                        double mean = 0.0;
                        for (MetricType metricTypeItem : metricType) {
                            if (metricTypeItem.getName().contains("Plot")) {
                                mean = metricTypeItem.getMean();

                            }
                        }
                        meanOfSimulations.add(mean);

                    }
                    double[] meanArrayOfMeans = new double[meanOfSimulations.size()];
                    for (int i = 0; i < meanOfSimulations.size(); i++) meanArrayOfMeans[i] = meanOfSimulations.get(i);

                    StatisticValues stat = new StatisticValues(meanArrayOfMeans);
                    statisticValues.put(value, stat);
                }
            }
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
