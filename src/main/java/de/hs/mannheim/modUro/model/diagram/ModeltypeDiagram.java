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

import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.ModelType;
import de.hs.mannheim.modUro.model.Simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for ModeltypeDiagram Model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
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
    private void createMetricTypeNameList() {
        metricTypeName = new ArrayList<>();

        for (Simulation simulationItem : modelType.getSimulations()) {
            for (MetricType metricTypeItem : simulationItem.getMetricTypes()) {

                if (!metricTypeName.contains(metricTypeItem.getName())) {
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
