package de.hs.mannheim.modUro.model.diagram;

import de.hs.mannheim.modUro.model.MetricType;
import de.hs.mannheim.modUro.model.Simulation;
import java.util.List;

/**
 * Class for SimulationDiagram Model.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class SimulationDiagram {

    private Simulation simulation;

    private String simulationName;
    private List<MetricType> metricTypes;

    public SimulationDiagram(Simulation simulation) {
        this.simulation = simulation;
        metricTypes = simulation.getMetricType();
        simulationName = simulation.getSimulationName();

    }

    public List<MetricType> getMetricTypes() {
        return metricTypes;
    }

    public String getSimulationName() {
        return simulationName;
    }
}
