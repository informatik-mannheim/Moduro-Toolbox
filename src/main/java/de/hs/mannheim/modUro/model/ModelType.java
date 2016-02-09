package de.hs.mannheim.modUro.model;

import java.util.List;

/**
 * Model class for a ModelType.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public class ModelType {

    private String name;
    private List<Simulation> simulations;


    public ModelType(String name, List<Simulation> simulations) {
        this.name = name;
        this.simulations = simulations;
    }

    public String getName() {
        return name;
    }

    public List<Simulation> getSimulations() {
        return simulations;
    }
}
