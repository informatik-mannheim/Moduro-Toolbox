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


import de.hs.mannheim.modUro.config.FitnessName;
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
 * @auhor Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class BoxAndWhiskerPlotModel {

    private Project project;

    private List<String> modelTypeName;
    private Map<String, StatisticValues> statisticValues;

    public BoxAndWhiskerPlotModel(Project project) {
        this.project = project;
        modelTypeName = listModelTypeName();
        statisticValues = new HashMap<>();
        calculateStatValues();
    }

    private void calculateStatValues() {

        for (ModuroModel moduroModelItem : project.getModuroModelList()) {
            List<Double> meanOfSimulations = new ArrayList<>();
            for (Simulation simulationItem : moduroModelItem.getSimulations()) {
                List<TimeSeries> metricType = simulationItem.getAllTimeSeries();
                double mean = 0.0;
                for (TimeSeries metricTypeItem : metricType) {
                    if (metricTypeItem.getName().equals(FitnessName.TOTAL_FITNESS.getName())) {
                        mean = metricTypeItem.getStats().getMean();
                    }
                }
                meanOfSimulations.add(mean);
            }
            Double[] a = meanOfSimulations.toArray(new Double[0]);
            double[] meanArrayOfMeans = Stream.of(a).mapToDouble(Double::doubleValue).toArray();

            StatisticValues stat =
                    new StatisticValues(moduroModelItem.getName(), meanArrayOfMeans);
            statisticValues.put(moduroModelItem.getName(), stat);
        }
    }

    /**
     * Lists distinct names of model type.
     *
     * @return
     */
    private List<String> listModelTypeName() {
        List<String> modeltypeName = new ArrayList<>();
        for (ModuroModel moduroModelItem : project.getModuroModelList()) {
            if (!modeltypeName.contains(moduroModelItem.getName())) {
                modeltypeName.add(moduroModelItem.getName());
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
