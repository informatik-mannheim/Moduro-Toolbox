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
package de.hs.mannheim.modUro.model.overview;

import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.StatisticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for Model Overview Model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeOverview {

    private ModelType modelType;

    private List<StatisticValues> metricTypes;
    private int numberOfSimulations;
    private int numberOfSteadyStateSimulation;
    private int numberOfAbortedSimulations;
    private int numberOfCompletedSimulations;
    private boolean isCompleted, isSteadState;
    private Map<String, StatisticValues> statisticValues;

    /**
     * Constructor.
     *
     * @param modelType
     */
    public ModeltypeOverview(ModelType modelType, boolean isCompleted,
                             boolean isSteadState) {
        this.modelType = modelType;
        this.isCompleted = isCompleted;
        this.isSteadState = isSteadState;
        metricTypes = listMetricTypes();
        numberOfSimulations = modelType.getSimulations().size();

        countSteadyStateSimulation();
        countCompletedSimulations();
        countAbortedSimulations();
        calculateStatisticValues();
    }

    /**
     * Lists distinct names of metric type.
     *
     * @return
     */
    private List<StatisticValues> listMetricTypes() {
        List<StatisticValues> metricTypes = new ArrayList<>();
        for (Simulation simulationItem : modelType.getSimulations()) {
            for (StatisticValues metricTypeItem : simulationItem.getMetricTypes()) {
                if (!metricTypes.contains(metricTypeItem)) {
                    metricTypes.add(metricTypeItem);
                }
            }
        }
        metricTypes.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));
        return metricTypes;
    }

    /**
     * Counts number of completed simulations.
     */
    private void countCompletedSimulations() {
        int count = 0;
        for (Simulation simulationItem : modelType.getSimulations()) {
            if (simulationItem.isCompleted()) {
                count++;
            }
        }
        this.numberOfCompletedSimulations = count;
    }

    /**
     * Counts number of aborted simulations.
     */
    private void countAbortedSimulations() {
        int count = 0;
        for (Simulation simulationItem : modelType.getSimulations()) {
            if (simulationItem.isInSteadyState()) {
                count++;
            }
        }
        this.numberOfSteadyStateSimulation = count;
    }

    /**
     * Counts number of simulations in steady state.
     */
    private void countSteadyStateSimulation() {
        int count = 0;
        for (Simulation simulationItem : modelType.getSimulations()) {
            if (simulationItem.isAborted()) {
                count++;
            }
        }
        this.numberOfAbortedSimulations = count;
    }

    /**
     * @param metricType
     * @return
     */
    private double[] getArrayByMetricName(StatisticValues metricType) {
        List<Double> mean = new ArrayList<>();

        for (Simulation simulation : modelType.getSimulations()) {
            // Filter:
            boolean addResults = true;
            if (isCompleted) {
                addResults = addResults && simulation.isCompleted();
            }
            if (isSteadState) {
                addResults = addResults && simulation.isInSteadyState();
            }

            if (addResults) {
                for (StatisticValues metricTypeItem : simulation.getMetricTypes()) {
                    if (metricTypeItem.getName().equals(metricType.getName())) {
                        mean.add(metricTypeItem.getMean());
                    }
                }
            }
        }
        double[] meanArray = new double[mean.size()];
        for (int i = 0; i < mean.size(); i++) meanArray[i] = mean.get(i);
        return meanArray;
    }

    /**
     * Calculates statisticValues for each MetricType for LineDiagram.
     */
    private void calculateStatisticValues() {
        statisticValues = new HashMap<>();

        for (StatisticValues metricType : metricTypes) {
            double[] array = getArrayByMetricName(metricType);
            StatisticValues statValue =
                    new StatisticValues(metricType.getName(), array);
            statisticValues.put(metricType.getName(), statValue);
        }
    }

    public int getNumberOfSimulations() {
        return numberOfSimulations;
    }

    public int getNumberOfSteadyStateSimulation() {
        return numberOfSteadyStateSimulation;
    }

    public int getNumberOfAbortedSimulations() {
        return numberOfAbortedSimulations;
    }

    public int getNumberOfCompletedSimulations() {
        return numberOfCompletedSimulations;
    }

    public Map<String, StatisticValues> getStatisticValues() {
        return statisticValues;
    }
}
