package de.hs.mannheim.modUro.model.diagram;

import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for ModeltypeDiagram Model.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModeltypeDiagram {

    private ModelType modelType;

    private List<String> metricTypeName;
    private List<Simulation> simulationList;

    public ModeltypeDiagram(ModelType modelType) {
        this.modelType = modelType;

        simulationList = modelType.getSimulations();

        createMetricTypeNameList();
    }

    /**
     * Creates a list of all MetricTypes in Modeltype.
     */
    private void createMetricTypeNameList(){
        metricTypeName = new ArrayList<>();

        for (Simulation simulationItem: modelType.getSimulations()) {
            for (MetricType metricTypeItem: simulationItem.getMetricType()) {

                if(!metricTypeName.contains(metricTypeItem.getName())){
                    metricTypeName.add(metricTypeItem.getName());
                }

            }
        }
    }

    public List<String> getMetricTypeName() {
        return metricTypeName;
    }

    public List<Simulation> getSimulationList() {
        return simulationList;
    }
}
