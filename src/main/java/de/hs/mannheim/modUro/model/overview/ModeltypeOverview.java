package de.hs.mannheim.modUro.model.overview;

import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Simulation;
import de.hs.mannheim.modUro.model.StatisticValues;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for Model Overview Model.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeOverview {

    // Get a DescriptiveStatistics instance (calculation of mean and stdDev)
    DescriptiveStatistics stats = new DescriptiveStatistics();

    private ModelType modelType;

    private List<String> metricTypeName;
    private int numberOfSimulations;
    private int numberOfSteadyStateSimulation;
    private int numberOfAbortedSimulations;
    private int numberOfCompletedSimulations;
    private List<StatisticValues> statisticValues;

    /**
     * Constructor.
     * @param modelType
     */
    public ModeltypeOverview(ModelType modelType){
        this.modelType = modelType;
        metricTypeName = new ArrayList<>();
        metricTypeName = listMetricTypeNames();
        numberOfSimulations = modelType.getSimulations().size();

        countSteadyStateSimulation();
        countCompletedSimulations();
        countAbortedSimulations();
        calculateStatisticValues();
    }

    /**
     * Lists distinct names of metric type.
     * @return
     */
    private List<String> listMetricTypeNames() {
        List<String> metricTypeName = new ArrayList<>();
        for (Simulation simulationItem: modelType.getSimulations()) {
            for (MetricType metricTypeItem: simulationItem.getMetricType()) {
                if(!metricTypeName.contains(metricTypeItem.getName())) {
                    metricTypeName.add(metricTypeItem.getName());
                }
            }
        }
        return metricTypeName;
    }

    /**
     * Counts number of completed simulations.
     */
    private void countCompletedSimulations() {
        int count = 0;
            for (Simulation simulationItem: modelType.getSimulations()) {
                if(simulationItem.isCompleted()) {
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
            for (Simulation simulationItem: modelType.getSimulations()) {
                if(simulationItem.isInSteadyState()) {
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
            for (Simulation simulationItem: modelType.getSimulations()) {
                if(simulationItem.isAborted()) {
                    count++;
                }
            }
        this.numberOfAbortedSimulations = count;
    }

    /**
     * Calculates Mean of all same metrictype
     * @param name
     * @return
     */
    private double returnMean(String name) {
        List<Double> mean = new ArrayList<>();

        for (Simulation simulation : modelType.getSimulations()) {
            for (MetricType metricTypeItem : simulation.getMetricType()) {
                if(metricTypeItem.getName().equals(name)) {
                    mean.add(metricTypeItem.getMean());
                }
            }
        }

        double[] meanArray = new double[mean.size()];
        for(int i = 0; i < mean.size(); i++) meanArray[i] = mean.get(i);

        for( int i = 0; i < meanArray.length; i++) {
            stats.addValue(meanArray[i]);
        }

        double meanOfMetricType = stats.getMean();
        stats.clear();

        return meanOfMetricType;
    }

    /**
     * Calculates stdDev of all same metrictype
     * @param name
     * @return
     */
    private double returnStdDev(String name) {
        List<Double> stdDev = new ArrayList<>();

            for (Simulation simulation : modelType.getSimulations()) {
                for (MetricType metricTypeItem : simulation.getMetricType()) {
                    if(metricTypeItem.getName().equals(name)) {
                        stdDev.add(metricTypeItem.getDeviation());
                    }
                }
            }

        double[] devArray = new double[stdDev.size()];
        for(int i = 0; i < stdDev.size(); i++) devArray[i] = stdDev.get(i);

        for( int i = 0; i < devArray.length; i++) {
            stats.addValue(devArray[i]);
        }

        double devOfMetricType = stats.getStandardDeviation();
        stats.clear();

        return devOfMetricType;
    }

    /**
     * Calculates statisticValues for each MetricType for LineDiagram.
     */
    private void calculateStatisticValues() {
        statisticValues = new ArrayList<>();

        for (String name : metricTypeName) {

            double mean = returnMean(name);
            double stdDev = returnStdDev(name);

            StatisticValues statValue = new StatisticValues(name, mean, stdDev);

            statisticValues.add(statValue);
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

    public List<StatisticValues> getStatisticValues() {
        return statisticValues;
    }
}
