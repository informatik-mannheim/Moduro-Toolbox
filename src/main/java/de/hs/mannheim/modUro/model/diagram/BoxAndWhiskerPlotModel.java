package de.hs.mannheim.modUro.model.diagram;


import de.hs.mannheim.modUro.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for BoxAndWhiskerPlotModel.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
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

    private void calculateStatValues(){
        List<Double> meanOfSimulations = new ArrayList<>();

        for (String value: modelTypeName) {
            for (ModelType modelTypeItem: project.getModelTypeList()) {
                if(modelTypeItem.getName().equals(value)) {
                    for (Simulation simultionItem : modelTypeItem.getSimulations()) {
                        List<MetricType> metricType = simultionItem.getMetricType();
                        double mean = 0.0;
                        for (MetricType metricTypeItem : metricType) {
                            if(metricTypeItem.getName().contains("Plot")) {
                                mean = metricTypeItem.getMean();

                            }
                        }
                        meanOfSimulations.add(mean);

                    }
                    double[] meanArrayOfMeans = new double[meanOfSimulations.size()];
                    for(int i = 0; i < meanOfSimulations.size(); i++) meanArrayOfMeans[i] = meanOfSimulations.get(i);

                    StatisticValues stat = new StatisticValues(meanArrayOfMeans);
                    statisticValues.put(value, stat);
                }
            }
        }
    }

    /**
     * Lists distinct names of model type.
     * @return
     */
    private List<String> listModelTypeName() {
        List<String> modeltypeName = new ArrayList<>();
        for (ModelType modelTypeItem: project.getModelTypeList()) {
                if(!modeltypeName.contains(modelTypeItem.getName())) {
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
