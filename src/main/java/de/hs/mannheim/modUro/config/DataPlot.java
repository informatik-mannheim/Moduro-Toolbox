package de.hs.mannheim.modUro.config;

/**
 * Configurations for DataPlot.
 * @author Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum DataPlot {

    MIN_TIME(40.0),     //min Time of a simulation to be in steady state
    MAX_TIME(700.0);    //max Time of a simulation to be done

    private final double value;

    DataPlot(double value) { this.value = value; }

    public double getValue() {return value;}
}
