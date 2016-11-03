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
import de.hs.mannheim.modUro.model.Simulation;

import java.util.List;

/**
 * Class for SimulationDiagram Model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationDiagram {

    private Simulation simulation;

    private String simulationName;
    private List<MetricType> metricTypes;

    public SimulationDiagram(Simulation simulation) {
        this.simulation = simulation;
        metricTypes = simulation.getMetricTypes();
        simulationName = simulation.getSimulationName();

    }

    public List<MetricType> getMetricTypes() {
        return metricTypes;
    }

    public String getSimulationName() {
        return simulationName;
    }
}
