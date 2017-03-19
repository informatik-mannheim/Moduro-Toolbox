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
import de.hs.mannheim.modUro.model.Simulation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for SimulationDiagram Model.
 *
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationDiagram {

    private Simulation simulation;

    private String simulationName;
    private List<TimeSeries> timeSeries;

    public SimulationDiagram(Simulation simulation) {
        this.simulation = simulation;
        timeSeries = simulation.getAllTimeSeries().stream().
                filter(e -> e instanceof TimeSeries).
                map(e -> (TimeSeries) e).collect(Collectors.toList());
        timeSeries.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));
        simulationName = simulation.getSimulationName();
    }

    public List<TimeSeries> getTimeSeries() {
        return timeSeries;
    }

    public String getSimulationName() {
        return simulationName;
    }
}
