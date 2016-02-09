package de.hs.mannheim.modUro.config;

/**
 * Configuration for file names.
 * @author  Mathuraa Pathmanathan (mathuraa@hotmail.de)
 */
public enum FileName {
    TOTAL_FITNESS_FILE("FitnessPlot.dat"),      //Name of total fitness file
    METRIC_DATA_FILE("ParameterDump.dat");      //Name of metric data file

    private final String name;

    FileName(String name) { this.name = name; }

    public String getName() {return name;}
}
