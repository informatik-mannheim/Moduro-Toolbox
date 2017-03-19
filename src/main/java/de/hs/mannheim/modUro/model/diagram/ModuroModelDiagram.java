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

import de.hs.mannheim.modUro.model.TimeSeries;
import de.hs.mannheim.modUro.model.ModuroModel;
import de.hs.mannheim.modUro.model.Simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for ModuroModelDiagram Model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModuroModelDiagram {

    private ModuroModel moduroModel;
    private List<String> metricTypeNames;
    private List<Simulation> simulationList;

    public ModuroModelDiagram(ModuroModel moduroModel) {
        this.moduroModel = moduroModel;
        simulationList = moduroModel.getSimulations();
        createMetricTypeNameList();
    }

    /**
     * Creates a list of all MetricTypes in Modeltype.
     */
    private void createMetricTypeNameList() {
        metricTypeNames = new ArrayList<>();

        for (Simulation simulationItem : moduroModel.getSimulations()) {
            for (TimeSeries timeSeries : simulationItem.getAllTimeSeries()) {
                if (!metricTypeNames.contains(timeSeries.getName())) {
                    metricTypeNames.add(timeSeries.getName());
                }
            }
        }
        metricTypeNames.sort(String::compareTo);
    }

    public List<String> getMetricTypeNames() {
        return metricTypeNames;
    }

    public List<Simulation> getSimulationList() {
        return simulationList;
    }
}
