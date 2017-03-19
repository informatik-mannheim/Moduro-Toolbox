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

import de.hs.mannheim.modUro.config.FileName;
import de.hs.mannheim.modUro.config.FilterOption;
import de.hs.mannheim.modUro.config.RegEx;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A moduro model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class ModuroModel {

    private String name;
    private List<Simulation> simulations;
    // List of all directories that contain this model:
    private List<File> dirList;
    private FilterOption filterOption;
    // private Map<String, TimeSeries> timeSeries = new HashMap<>();
    private Map<String, StatisticValues> statisticValues = new HashMap<>();

    private List<String> metricTypes;
    private int numberOfSteadyStateSimulation;
    private int numberOfAbortedSimulations;
    private int numberOfCompletedSimulations;

    public ModuroModel(List<File> dirList, FilterOption filterOption) {
        this.dirList = dirList;
        this.filterOption = filterOption;
        this.name = createModelTypeName();
        this.simulations = createSimulationList();
        metricTypes = listMetricTypes();

        countSteadyStateSimulation();
        countCompletedSimulations();
        countAbortedSimulations();
        calculateStatisticValues();
    }

    public String getName() {
        return name;
    }

    public List<Simulation> getSimulations() {
        return simulations;
    }

    /*
    public Map<String, TimeSeries> getTimeSeries() {
        return timeSeries;
    }

    public List<TimeSeries> getAllTimeSeries() {
        return new ArrayList(timeSeries.values());
    }
    */

    public List<StatisticValues> getStats() {
        return new ArrayList(statisticValues.values());
    }

    public StatisticValues getStatsByName(String name) {
        return statisticValues.get(name);
    }

    public int getNumberOfSimulations() {
        return getSimulations().size();
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


    /**
     * Creates name of Modeltype.
     *
     * @return
     */
    private String createModelTypeName() {
        String[] tokenValue =
                dirList.get(0).getName().split(RegEx.MODEL_TYPE_REG_EX.getName());
        return tokenValue[0];
    }

    /**
     * Creates SimulationList of the modeltype.
     *
     * @return
     */
    private List<Simulation> createSimulationList() {
        List<Simulation> simulationList = new ArrayList<>();

        for (File file : dirList) {
            if (file.isDirectory() && directoryContainsMetricDataFile(file)) {
                Simulation simulation = new Simulation(file);
                // Apply filter:
                boolean inResult = true;
                if (filterOption.completed) {
                    inResult = inResult && simulation.isCompleted();
                }
                if (filterOption.steadyState) {
                    inResult = inResult && simulation.isInSteadyState();
                }
                if (filterOption.fromDate != null) {
                    inResult = inResult &&
                            filterOption.fromDate.
                            isBefore(simulation.getStartTime().toLocalDate());
                }
                if (filterOption.toDate != null) {
                    inResult = inResult &&
                            filterOption.toDate.
                                    isAfter(simulation.getStartTime().toLocalDate());
                }
                if (inResult) {
                    simulationList.add(simulation);
                }
            }
        }
        return simulationList;
    }

    /**
     * Checks if directory contains a parameter dump file
     *
     * @param file
     * @return
     */
    private boolean directoryContainsMetricDataFile(File file) {
        boolean containsFitnessPlot = false;
        File[] allFiles = file.listFiles();
        for (File fileItem : allFiles) {
            if (fileItem.getName().equals(FileName.PARAMETER_DUMP.getName())) {
                containsFitnessPlot = true;
            }
        }
        return containsFitnessPlot;
    }

    /**
     * Lists distinct names of metric type.
     *
     * @return
     */
    private List<String> listMetricTypes() {
        List<String> metricTypes = new ArrayList<>();
        for (Simulation simulationItem : getSimulations()) {
            for (TimeSeries metricTypeItem : simulationItem.getAllTimeSeries()) {
                if (!metricTypes.contains(metricTypeItem.getName())) {
                    metricTypes.add(metricTypeItem.getName());
                }
            }
        }
        metricTypes.sort((e1, e2) -> e1.compareTo(e2));
        return metricTypes;
    }

    /**
     * Counts number of completed simulations.
     */
    private void countCompletedSimulations() {
        int count = 0;
        for (Simulation simulationItem : getSimulations()) {
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
        for (Simulation simulationItem : getSimulations()) {
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
        for (Simulation simulationItem : getSimulations()) {
            if (simulationItem.isAborted()) {
                count++;
            }
        }
        this.numberOfAbortedSimulations = count;
    }

    /**
     * Calculates statisticValues for each TimeSeries for LineDiagram.
     */
    private void calculateStatisticValues() {

        for (String name : metricTypes) {
            List<Double> l = new ArrayList<>();
            for (Simulation simulation : simulations) {
                TimeSeries timeSeries = simulation.getTimeSeriesByName(name);
                if (timeSeries != null) {
                    l.add(timeSeries.getStats().getMean());
                }
            }
            Double[] L = l.toArray(new Double[0]);
            double[] array = Stream.of(L).mapToDouble(Double::doubleValue).toArray();
            StatisticValues statValue = new StatisticValues(name, array);
            statisticValues.put(name, statValue);
        }
    }
}
